package pl.edu.uj.tcs.memoizer.plugins.communication;

import pl.edu.uj.tcs.memoizer.events.IEvent;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class MemeDownloadedEvent implements IEvent {
	
	private Meme memeInfo;

	public MemeDownloadedEvent(Meme memeInfo) {

		this.memeInfo = memeInfo;
	}
	
	public Meme getMeme() {
		return memeInfo;
	}

	public IDownloadPlugin getPlugin() {
		return (IDownloadPlugin) memeInfo.getOwner();
	}

	public EViewType getViewType() {
		return memeInfo.getViewType();
	}
}
