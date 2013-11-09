package pl.edu.uj.tcs.memoizer.plugins.communication;

import pl.edu.uj.tcs.memoizer.events.IEvent;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.MemeInfo;

public class MemeDownloadedEvent implements IEvent {
	
	private MemeInfo memeInfo;
	private IDownloadPlugin plugin;
	private EViewType viewType;

	public MemeDownloadedEvent(MemeInfo memeInfo, IDownloadPlugin plugin, EViewType viewType) {

		this.memeInfo = memeInfo;
		this.plugin = plugin;
		this.viewType = viewType;
	}
	
	public MemeInfo getMemeInfo() {
		return memeInfo;
	}

	public IDownloadPlugin getPlugin() {
		return plugin;
	}

	public EViewType getViewType() {
		return viewType;
	}

}
