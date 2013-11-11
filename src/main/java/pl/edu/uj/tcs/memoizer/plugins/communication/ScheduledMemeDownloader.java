package pl.edu.uj.tcs.memoizer.plugins.communication;

import static pl.edu.uj.tcs.memoizer.plugins.communication.PluginConnectorSettings.DEFAULT_UNSELECTED_REFRESH_RATE;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.events.exceptions.EventException;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;

public class ScheduledMemeDownloader implements IScheduledMemeDownloader {
	
	private IDownloadPlugin plugin;
	private IEventService eventService;

	private final static Logger LOG = Logger.getLogger(Downloader.class);

	private long refreshRate = DEFAULT_UNSELECTED_REFRESH_RATE;
	
	private ScheduledThreadPoolExecutor threadPool;
	
	public ScheduledMemeDownloader(IDownloadPlugin plugin, IEventService eventService) {
		this.plugin = plugin;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		if(threadPool == null || threadPool.isShutdown()) {
			threadPool = new ScheduledThreadPoolExecutor(1);
			threadPool.scheduleAtFixedRate(new Downloader(), 0, refreshRate, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public void stop() {
		
		if(threadPool != null && !threadPool.isShutdown()) {
			threadPool.shutdown();
		}
	}
	
	@Override
	public boolean isRunning() {
		
		return (threadPool != null && !threadPool.isShutdown());
	}

	@Override
	public IDownloadPlugin getPlugin() {
		return plugin;
	}
	
	@Override
	public void setMinRefreshRate(long time) {

		if(isRunning()) {
			stop();
			refreshRate = time;
			start();
		} else {
			refreshRate = time;
		}
	}

	@Override
	public long getMinRefreshRate() {
		return refreshRate;
	}
	
	private class Downloader implements Runnable {
		
		private IEventService eventService = ScheduledMemeDownloader.this.eventService;
		private IDownloadPlugin plugin = ScheduledMemeDownloader.this.plugin;
		private Logger LOG = ScheduledMemeDownloader.this.LOG;

		@Override
		public void run() {
			//Iterable<Meme> memes = ScheduledMemeDownloader.this.plugin.getRecordsSinceLast(); // to change
			// TODO dumb implementation - fill with proper solution
			try {
				eventService.call(new MemeDownloadedEvent(null, plugin, EViewType.CHRONOLOGICAL));
			} catch (EventException e) {
				LOG.error(e.getMessage());
			} 
		}
	}
	
}
