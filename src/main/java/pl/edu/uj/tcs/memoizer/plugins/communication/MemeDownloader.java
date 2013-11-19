package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.concurrent.Callable;

import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class MemeDownloader implements Callable<Meme> {
	
	private IDownloadPlugin plug;
	
	public MemeDownloader(IDownloadPlugin plug) {
		this.plug = plug;
	}

	@Override
	public Meme call() throws Exception {
		return plug.getNext();
	}

}
