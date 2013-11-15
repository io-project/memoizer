package pl.edu.uj.tcs.memoizer.plugins.communication;

import static pl.edu.uj.tcs.memoizer.plugins.communication.PluginConnectorSettings.DEFAULT_SELECTED_REFRESH_RATE;
import static pl.edu.uj.tcs.memoizer.plugins.communication.PluginConnectorSettings.DEFAULT_UNSELECTED_REFRESH_RATE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
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
	private Map<IDownloadPlugin, IScheduledMemeDownloader> downloaders = new TreeMap<>(); 
	private Map<EViewType, BlockingQueue<Meme>> buffers = new HashMap<>();

	private IHandler<Meme> guiHandler;
	
	public MemeProvider(IEventService eventService, IHandler<Meme> guiHandler) {

		this.eventService = eventService;
		this.guiHandler = guiHandler;
		
		eventService.attach(this);
		
		for(EViewType vt: EViewType.values()) {
			buffers.put(vt, new LinkedBlockingDeque<Meme>());
		}
	}


	@Override
	public void setView(IPluginView view, List<IDownloadPlugin> plugins) throws InvalidPlugin {

		if(checkPlugins(view.getViewType(), plugins)) {

			
			synchronized(pluginView) {
				for(IDownloadPlugin plugin: plugins) {

					plugin.setView(view.getViewType());

					IScheduledMemeDownloader downloader = new ScheduledMemeDownloader(plugin, eventService); 

					downloader.setMinRefreshRate(DEFAULT_SELECTED_REFRESH_RATE);
					downloader.start();
					
					downloaders.put(plugin, downloader);
					
					for(Entry<IDownloadPlugin, IScheduledMemeDownloader> entry: downloaders.entrySet()) {
						if(entry.getValue().isRunning() && !plugins.contains(entry.getKey())) {
							entry.getValue().setMinRefreshRate(DEFAULT_UNSELECTED_REFRESH_RATE);
						}
					}
				}
				pluginView = view;
				provideEnqueued();
			}

		} else {
			throw new InvalidPlugin("At least on of chosen plugins does not provide view: " 
					+ view.getViewType().getName());
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
		for(BlockingQueue<Meme> que: buffers.values()) {
			que.clear();
		}
	}
	
	@Override
	public void cancel() {
		LOG.debug("Stopping meme provider");

		for(Entry<IDownloadPlugin, IScheduledMemeDownloader> entry: downloaders.entrySet()) {
			entry.getValue().stop();
		}
		downloaders.clear();
		
		clear();
	}

	
	private void provideEnqueued() { // in synchronized(pluginView) block

		ArrayList<Meme> results = new ArrayList<Meme>();
		BlockingQueue<Meme> que = buffers.get(pluginView.getViewType());
		
		que.drainTo(results);
		while(!results.isEmpty()) {
			guiHandler.handle(pluginView.extractNextMeme(results));
		}
	}
	
	private void enqueue(MemeDownloadedEvent event) {
		// TODO limit number of elements in queues by stopping all slowing down downloaders
		BlockingQueue<Meme> que = buffers.get(event.getViewType());
		que.add(event.getMeme());
	}

	@Override
	public void notify(MemeDownloadedEvent event) {
		LOG.debug("Calling notify on plugin connector with new message: " + event.getMeme().getDescription());
		
		enqueue(event);
		synchronized(pluginView) {

			if(pluginView != null && pluginView.getViewType() == event.getViewType()) {
				provideEnqueued();
			} 
		}
	}
}
