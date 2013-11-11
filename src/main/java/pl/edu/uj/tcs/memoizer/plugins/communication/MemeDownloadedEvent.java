package pl.edu.uj.tcs.memoizer.plugins.communication;

import pl.edu.uj.tcs.memoizer.events.IEvent;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class MemeDownloadedEvent implements IEvent {
	
	private Meme memeInfo;
	private IDownloadPlugin plugin;
	private EViewType viewType;

	public MemeDownloadedEvent(Meme memeInfo, IDownloadPlugin plugin, EViewType viewType) {

		this.memeInfo = memeInfo;
		this.plugin = plugin;
		this.viewType = viewType;
	}
	
	public Meme getMeme() {
		return memeInfo;
	}

	public IDownloadPlugin getPlugin() {
		return plugin;
	}

	public EViewType getViewType() {
		return viewType;
	}

}
