package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.gui.MetadataHandler;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class MemeProviderUnseen implements IMemeProvider {
	
	private static final Logger LOG = Logger.getLogger(MemeProvider.class);
	private static final int MIN_LIMIT = 10;
	private static final long AWAIT_TIME = 50;
	
	private IPluginView pluginView;
	private ExecutorService execService = Executors.newCachedThreadPool();
	
	private ArrayList<IDownloadPlugin> plugs = new ArrayList<>();

	private HashMap<EViewType, LinkedList<Meme>> extracted = new HashMap<>();
	private HashMap<EViewType, Map<String, List<Future<Iterable<Meme>>>>> awaiting = new HashMap<>();
	
	private LinkedList<Meme> currExtr;
	private MetadataHandler metadataHandler = null;
	
	public MemeProviderUnseen(MetadataHandler metadataHandler) {
		
		this.metadataHandler = metadataHandler;
		
		currExtr = new LinkedList<Meme>();

		for(EViewType vt: EViewType.values()) {
			extracted.put(vt, new LinkedList<Meme>());
			awaiting.put(vt, new HashMap<String, List<Future<Iterable<Meme>>>>());
		}
	}

	@Override
	public void setView(IPluginView view, List<IDownloadPlugin> newPlugs) throws InvalidPluginException {

		if(checkPlugins(view.getViewType(), newPlugs)) {
			
			refillExtracted(newPlugs, view.getViewType());

			plugs.clear();
			plugs.addAll(newPlugs);
			
			pluginView = view;
			
			if(currExtr.size() < MIN_LIMIT) {
				downloadNext(MIN_LIMIT);
			}
			
		} else {
			throw new InvalidPluginException("At least on of chosen plugins does not provide view: "
                    + view.getViewType().getName());
		}
	}
	
	private static boolean plugInList(List<IDownloadPlugin> list, String plug) {

		for(IDownloadPlugin dp: list) {
			if(plug.equals(dp.getServiceName())) return true;
		}

		return false;
	}
	
	private void refillExtracted(List<IDownloadPlugin> newPlugs, EViewType newVt) {

		LinkedList<Meme> inExtr = extracted.get(newVt);
		int count = inExtr.size();

		if(pluginView != null && newVt == pluginView.getViewType()) {

			Iterator<Meme> it = currExtr.iterator();
			while(it.hasNext()) {
	
				Meme m = it.next();
				if(!plugInList(newPlugs, m.getServiceName())) {
					inExtr.addLast(m);
					it.remove();
				}
			}
		} else if(pluginView != null) {

			LinkedList<Meme> buffered = extracted.get(pluginView.getViewType());
			buffered.addAll(currExtr);
			currExtr.clear();
		}
			
		int i = 0;
		Iterator<Meme> it = inExtr.iterator();
		while(it.hasNext() && i < count) {

			Meme m = it.next();
			if(plugInList(newPlugs, m.getServiceName())) {
				currExtr.addLast(m);
				it.remove();
			}
			i++;
		}
	}
	
	private static boolean checkPlugins(EViewType viewType, List<IDownloadPlugin> plugins) {
		
		for(IDownloadPlugin plugin: plugins) {
			if(!plugin.getView().equals(viewType)) return false;
		}

		return true;
	}

	@Override
	public IPluginView getCurrentView() {
		return pluginView;
	}

	private void clear() {

		plugs.clear();
		extracted.clear();
		currExtr.clear();
		awaiting.clear();
	}
	
	@Override
	public void stop() {

		clear();
		execService.shutdown();
	}
	
	private void downloadForPlug(IDownloadPlugin plug, int count) {
		
		Map<String, List<Future<Iterable<Meme>>>> buffers = awaiting.get(pluginView.getViewType());
		List<Future<Iterable<Meme>>> futures = buffers.get(plug.getServiceName());
		if(futures == null) {
			futures = new LinkedList<Future<Iterable<Meme>>>();
			buffers.put(plug.getServiceName(), futures);
		}

		futures.add(execService.submit(new MemeDownloader(plug, count)));
	}

	private void downloadNext(int count) {
		
		int one_count = (plugs.size() + count - 1)/count;
		for(IDownloadPlugin plug: plugs) {
			downloadForPlug(plug, one_count);
		}
	}
	
	private void extractFinished() throws ExecutionException {

		Map<String, List<Future<Iterable<Meme>>>> buffers = awaiting.get(pluginView.getViewType());
		List<Future<Iterable<Meme>>> l;
		Iterator<Future<Iterable<Meme>>> it;
		Future<Iterable<Meme>> val;

		for(IDownloadPlugin plug: plugs) {
			l = buffers.get(plug.getServiceName());
			if(l == null) continue;
			
			it = l.iterator();
			while(it.hasNext()) {

				val = it.next();
				if(val.isDone()) {

					it.remove();
					try {
						Iterable<Meme> memes = val.get();
						if(memes != null) {
							appendExtracted(memes);
						}
					} catch (InterruptedException e) {
						LOG.error("Could not get Meme from plugin: " + plug.getServiceName() + 
								" due to an InterruptedException exception: " + e.getMessage());
					} 
				}
			}
		}
	}
	
	private boolean appendExtracted(Iterable<Meme> memes) {
		boolean cos = false;
		for(Meme m: memes) {
			if(m != null&&!metadataHandler.isSeened(m)) {
				cos = true;
				currExtr.addLast(m);
			}
		}
		return cos;
	}
	
	private void waitExtract(long time) throws ExecutionException {

		Map<String, List<Future<Iterable<Meme>>>> buffers = awaiting.get(pluginView.getViewType());
		List<Future<Iterable<Meme>>> l;
		Iterator<Future<Iterable<Meme>>> it;
		Future<Iterable<Meme>> val;

		for(IDownloadPlugin plug: plugs) {
			l = buffers.get(plug.getServiceName());
			if(l == null) continue;
			
			it = l.iterator();
			while(it.hasNext()) {

				val = it.next();

				try {
					Iterable<Meme> memes;
					try {
						memes = val.get(AWAIT_TIME, TimeUnit.MILLISECONDS);
						it.remove();
					} catch (TimeoutException e) {
						continue;
					}
					if(memes != null) {
						appendExtracted(memes);
					}
				} catch (InterruptedException e) {
					LOG.error("Could not get Meme from plugin: " + plug.getServiceName() + 
							" due to an InterruptedException exception: " + e.getMessage());
				} 
			}
		}
	}
	
	@Override
	public boolean hasNext() {
		
		if(pluginView == null) {
			return false;
		}
		if(!currExtr.isEmpty()) {
			return true;
		}
		
		try {
			extractFinished();
			if(currExtr.isEmpty()) {
	
				waitExtract(AWAIT_TIME);
				if(currExtr.isEmpty()) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} catch (ExecutionException e) {
			LOG.error("Could not check next memes: " + ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	public void waitForOne() throws ExecutionException {

		Map<String, List<Future<Iterable<Meme>>>> buffers = awaiting.get(pluginView.getViewType());
		for(IDownloadPlugin plug: plugs) {
			
			List<Future<Iterable<Meme>>> l = buffers.get(plug.getServiceName());
			if(l != null) {

				Iterator<Future<Iterable<Meme>>> it = l.iterator();
				while(it.hasNext()) {
					Future<Iterable<Meme>> ft = it.next();
					it.remove();
					try {
						Iterable<Meme> memes = ft.get();
						
						if(memes != null) {
							if(appendExtracted(memes))
								return;
						}
					} catch (InterruptedException e) {
						LOG.error("Could not get Meme: " + ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
	}

	@Override
	public Meme getNext() throws ExecutionException, DownloadMemeException {
		
		List<Meme> l = getNext(1);
		if(l.isEmpty()) {
			throw new DownloadMemeException("No new memes to return");
		} else {
			return l.get(0);
		}
	}

	@Override
	public List<Meme> getNext(int n) throws ExecutionException, DownloadMemeException {

		ArrayList<Meme> results = new ArrayList<Meme>();
		if(currExtr.size() < n) {
			extractFinished();
			if(currExtr.size() < n) {
				waitExtract(AWAIT_TIME);
			}
		}

		// at least one should be downloaded if possible
		for(int tries = 0; tries< 3; tries++){
			if(currExtr.isEmpty()) {
			
				waitForOne();
				if(!currExtr.isEmpty()) {
					extractFinished();
				}
			}
	
			int i = 0;
			while(i < n && !currExtr.isEmpty()) {
				results.add(pluginView.extractNextMeme(currExtr));
				i++;
			}
			
			if(currExtr.size() < MIN_LIMIT) {
				downloadNext(MIN_LIMIT);
			}
	
			if(!results.isEmpty())
				return results;
			
			System.out.println("TRY: "+tries);
		}
		throw new DownloadMemeException("No new memes to return");
	}

}