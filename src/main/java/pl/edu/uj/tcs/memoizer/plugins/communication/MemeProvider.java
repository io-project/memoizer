package pl.edu.uj.tcs.memoizer.plugins.communication;

import static pl.edu.uj.tcs.memoizer.plugins.communication.MemeProviderSettings.DEFAULT_SELECTED_REFRESH_RATE;
import static pl.edu.uj.tcs.memoizer.plugins.communication.MemeProviderSettings.DEFAULT_UNSELECTED_REFRESH_RATE;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.events.IEventObserver;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.handlers.IHandler;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class MemeProvider implements IMemeProvider, IEventObserver<MemeDownloadedEvent> {
	
	private static final Logger LOG = Logger.getLogger(MemeProvider.class);
	
	private IPluginView pluginView;

	private IEventService eventService;

	// relates on references
	private Map<String, IScheduledMemeDownloader> downloaders = new TreeMap<>(); 
	private Set<String> activePlugins = new HashSet<>();
	private Map<EViewType, LinkedBlockingDeque<Meme>> selectedBuf = new HashMap<>();
	private Map<EViewType, LinkedList<Meme>> idleBuf = new HashMap<>();

	public MemeProvider(IEventService eventService) {

		this.eventService = eventService;
		
		eventService.attach(this);
		
		for(EViewType vt: EViewType.values()) {
			selectedBuf.put(vt, new LinkedBlockingDeque<Meme>());
			idleBuf.put(vt, new LinkedList<Meme>());
		}
	}


	@Override
	public void setView(IPluginView view, List<IDownloadPlugin> plugins) throws InvalidPlugin {

		if(checkPlugins(view.getViewType(), plugins)) {

			synchronized(this) {
				
				activePlugins.clear();
				
				for(IDownloadPlugin plugin: plugins) {
					activePlugins.add(plugin.getName());
					plugin.setView(view.getViewType());

					IScheduledMemeDownloader downloader = downloaders.get(plugin.getName()); 
					if(downloader == null) {
						downloader = new ScheduledMemeDownloader(plugin, eventService);
						downloaders.put(plugin.getName(), downloader);
					}

					downloader.setMinRefreshRate(DEFAULT_SELECTED_REFRESH_RATE);
					downloader.start();
				}

				for(Entry<String, IScheduledMemeDownloader> entry: downloaders.entrySet()) {
					if(entry.getValue().isRunning() && !activePlugins.contains(entry.getKey())) {
						entry.getValue().setMinRefreshRate(DEFAULT_UNSELECTED_REFRESH_RATE);
					}
				}
				
				pluginView = view;
				refillQueues();
			}

		} else {
			throw new InvalidPlugin("At least on of chosen plugins does not provide view: " 
					+ view.getViewType().getName());
		}
	}
	
	private void refillQueues() {

		Deque<Meme> act = selectedBuf.get(pluginView.getViewType());
		LinkedList<Meme> idle = idleBuf.get(pluginView.getViewType());
		
		Iterator<Meme> it = act.iterator();
		int count = 0;
		while(it.hasNext()) {
			Meme m = it.next();
			if(!activePlugins.contains(m.getOwner().getName())) {
				it.remove();
				idle.add(m);
				count++;
			}
		}
		
		it = idle.iterator();
		int size = idleBuf.size() - count, i = 0;
		while(it.hasNext() && i < size) {
			Meme m = it.next();
			if(activePlugins.contains(m.getOwner().getName())) {
				it.remove();
				act.add(m);
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
		for(Queue<Meme> que: selectedBuf.values()) {
			que.clear();
		}
		for(Queue<Meme> que: idleBuf.values()) {
			que.clear();
		}
	}
	
	@Override
	public void stop() {
		LOG.debug("Stopping meme provider");

		for(Entry<String, IScheduledMemeDownloader> entry: downloaders.entrySet()) {
			entry.getValue().stop();
		}
		downloaders.clear();
		
		clear();
	}
	
	@Override
	public boolean hasNext() {
		Queue<Meme> que = selectedBuf.get(pluginView.getViewType());
		return !que.isEmpty();
	}

	@Override
	public Meme getNext() {

		List<Meme> res = getNext(1);
		
		if(res.isEmpty()) {
			return null;
		}
		return res.get(0);
	}

	@Override
	public List<Meme> getNext(int n) {

		ArrayList<Meme> memes = new ArrayList<Meme>();
		ArrayList<Meme> results = new ArrayList<Meme>();

		LinkedBlockingDeque<Meme> deq = selectedBuf.get(pluginView.getViewType());

		deq.drainTo(memes);
		
		for(int i = 0; i < n && !memes.isEmpty(); i++) {
			results.add(pluginView.extractNextMeme(memes));
		}

		for(Meme m: memes) {
			try {
				deq.putFirst(m);
			} catch (InterruptedException e) {
				LOG.error("Could not return Meme to the buffer");
			}
		}

		return results;
	}

	
	private void enqueue(MemeDownloadedEvent event) {
		// TODO limit number of elements in queues by stopping all slowing down downloaders
		if(activePlugins.contains(event.getPlugin().getName())) {
			Queue<Meme> que = selectedBuf.get(event.getViewType());
			que.add(event.getMeme());
		} else {
			Queue<Meme> que = idleBuf.get(event.getViewType());
			que.add(event.getMeme());
		}
	}

	@Override
	public void notify(MemeDownloadedEvent event) {
		LOG.debug("Calling notify on plugin connector with new message: " + event.getMeme().getDescription());
		
		synchronized(this) {

			enqueue(event);
		}
	}
}
