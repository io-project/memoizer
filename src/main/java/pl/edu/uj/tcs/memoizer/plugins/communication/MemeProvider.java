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

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class MemeProvider implements IMemeProvider {
	
	private static final Logger LOG = Logger.getLogger(MemeProvider.class);
	private static final int MIN_LIMIT = 10;
	private static final long AWAIT_TIME = 50;
	
	private IPluginView pluginView;
	private ExecutorService execService = Executors.newCachedThreadPool();
	
	private ArrayList<IDownloadPlugin> plugs = new ArrayList<>();

	private HashMap<EViewType, LinkedList<Meme>> extracted = new HashMap<>();
	private HashMap<EViewType, Map<String, List<Future<Meme>>>> awaiting = new HashMap<>();
	
	private LinkedList<Meme> currExtr;

	public MemeProvider() {
		
		currExtr = new LinkedList<Meme>();

		for(EViewType vt: EViewType.values()) {
			extracted.put(vt, new LinkedList<Meme>());
			awaiting.put(vt, new HashMap<String, List<Future<Meme>>>());
		}
	}

	@Override
	public void setView(IPluginView view, List<IDownloadPlugin> newPlugs) throws InvalidPlugin {

		if(checkPlugins(view.getViewType(), newPlugs)) {
			
			refillExtracted(newPlugs, view.getViewType());

			plugs.clear();
			plugs.addAll(newPlugs);
			
			pluginView = view;
			
			if(currExtr.size() < MIN_LIMIT) {
				downloadNext(MIN_LIMIT);
			}
			
		} else {
			throw new InvalidPlugin("At least on of chosen plugins does not provide view: "
                    + view.getViewType().getName());
		}
	}
	
	private static boolean plugInList(List<IDownloadPlugin> list, IDownloadPlugin plug) {

		for(IDownloadPlugin dp: list) {
			if(plug.getName().equals(dp.getName())) return true;
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
				if(!plugInList(newPlugs, (IDownloadPlugin) m.getOwner())) {
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
			if(plugInList(newPlugs, (IDownloadPlugin) m.getOwner())) {
				currExtr.addLast(m);
				it.remove();
			}
			i++;
		}
	}
	
	private static boolean checkPlugins(EViewType viewType, List<IDownloadPlugin> plugins) {
		
		for(IDownloadPlugin plugin: plugins) {
			if(!plugin.getAvailableViews().contains(viewType)) return false;
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

		execService.shutdown();
	}
	
	private void downloadForPlug(IDownloadPlugin plug, int count) {
		
		Map<String, List<Future<Meme>>> buffers = awaiting.get(pluginView.getViewType());
		List<Future<Meme>> futures = buffers.get(plug.getName());
		if(futures == null) {
			futures = new LinkedList<Future<Meme>>();
			buffers.put(plug.getName(), futures);
		}

		for(int i = 0; i < count; i++) {
			if(plug.hasNext()) {
				currExtr.add(plug.getNext());
			} else {
				futures.add(execService.submit(new MemeDownloader(plug)));
			}
		}
	}

	private void downloadNext(int count) {
		
		int one_count = (plugs.size() + count - 1)/count;
		for(IDownloadPlugin plug: plugs) {
			downloadForPlug(plug, one_count);
		}
	}
	
	private void extractFinished() {

		Map<String, List<Future<Meme>>> buffers = awaiting.get(pluginView.getViewType());
		List<Future<Meme>> l;
		Iterator<Future<Meme>> it;
		Future<Meme> val;

		for(IDownloadPlugin plug: plugs) {
			l = buffers.get(plug.getName());
			if(l == null) continue;
			
			it = l.iterator();
			while(it.hasNext()) {

				val = it.next();
				if(val.isDone()) {

					it.remove();
					try {
						Meme m = val.get();
						if(m != null) {
							currExtr.addLast(m);
						}
					} catch (InterruptedException e) {
						LOG.error("Could not get Meme from plugin: " + plug.getName() + 
								" due to an InterruptedException exception: " + e.getMessage());
					} catch (ExecutionException e) {
						LOG.error("Could not get Meme from plugin: " + plug.getName() + 
								" due to an ExecutionException exception: " + e.getMessage());
					}
				}
			}
		}
	}
	
	private void waitExtract(long time) {

		Map<String, List<Future<Meme>>> buffers = awaiting.get(pluginView.getViewType());
		List<Future<Meme>> l;
		Iterator<Future<Meme>> it;
		Future<Meme> val;

		for(IDownloadPlugin plug: plugs) {
			l = buffers.get(plug.getName());
			if(l == null) continue;
			
			it = l.iterator();
			while(it.hasNext()) {

				val = it.next();

				try {
					Meme m;
					try {
						m = val.get(AWAIT_TIME, TimeUnit.MILLISECONDS);
						it.remove();
					} catch (TimeoutException e) {
						continue;
					}
					if(m != null) {
						currExtr.addLast(m);
					}
				} catch (InterruptedException e) {
					LOG.error("Could not get Meme from plugin: " + plug.getName() + 
							" due to an InterruptedException exception: " + e.getMessage());
				} catch (ExecutionException e) {
					LOG.error("Could not get Meme from plugin: " + plug.getName() + 
							" due to an ExecutionException exception: " + e.getMessage());
				}
			}
		}
	}
	
	@Override
	public boolean hasNext() {

		if(!currExtr.isEmpty()) {
			return true;
		}
		
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
	}

	@Override
	public Meme getNext() {
		
		List<Meme> l = getNext(1);
		if(l.isEmpty()) {
			return null;
		} else {
			return l.get(0);
		}
	}
	
	public void waitForOne() {
		Map<String, List<Future<Meme>>> buffers = awaiting.get(pluginView.getViewType());
		
		boolean success = false;
		while(!success) {

			for(IDownloadPlugin plug: plugs) {

				List<Future<Meme>> l = buffers.get(plug.getName());
				if(l != null) {

					Iterator<Future<Meme>> it = l.iterator();
					while(it.hasNext()) {
						Future<Meme> ft = it.next();
						it.remove();
						try {
							Meme m = ft.get();
							if(m != null) {
								currExtr.addLast(m);
							}
							success = true;
							break;
						} catch (InterruptedException | ExecutionException e) {
							LOG.error("Could not get Meme: " + e.getMessage());
						}
					}
					if(success) break;
				}
			}
		}
	}

	@Override
	public List<Meme> getNext(int n) {

		ArrayList<Meme> results = new ArrayList<Meme>();
		if(currExtr.size() < n) {
			extractFinished();
			if(currExtr.size() < n) {
				waitExtract(AWAIT_TIME);
			}
		}

		// at least one should be downloaded if possible
		if(currExtr.isEmpty()) {
			waitForOne();
			if(!currExtr.isEmpty()) {
				extractFinished();
			}
		}

		if(currExtr.isEmpty()) {
			LOG.debug("Returning empty list");
		}
		
		int i = 0;
		while(i < n && !currExtr.isEmpty()) {
			results.add(pluginView.extractNextMeme(currExtr));
		}
		
		if(currExtr.size() < MIN_LIMIT) {
			downloadNext(MIN_LIMIT);
		}

		return results;
	}

}