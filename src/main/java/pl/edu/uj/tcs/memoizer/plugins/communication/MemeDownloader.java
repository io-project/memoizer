package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.concurrent.Callable;

import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class MemeDownloader implements Callable<Iterable<Meme>> {
	
	private IDownloadPlugin plug;
	private int count;
	
	public MemeDownloader(IDownloadPlugin plug, int count) {
		this.count = count;
		this.plug = plug;
	}

	@Override
	public Iterable<Meme> call() throws Exception {

		if(plug.hasNext()) {
			return plug.getNext(count);
		}
		return null;
	}

}
