package pl.edu.uj.tcs.memoizer.plugins.communication;

import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;

public interface IMemeDownloader {

	void start();

	void stop();
	
	boolean isRunning();
	
	IDownloadPlugin getPlugin();

}
