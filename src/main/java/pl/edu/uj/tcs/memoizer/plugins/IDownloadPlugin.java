package pl.edu.uj.tcs.memoizer.plugins;

import java.lang.*;

/*
 * Interface implemented by download plugin
 * @author pmikos (sokar92)
 */
public interface IDownloadPlugin extends IPlugin {
	
	/*
	 * Returns all memes since last update
	 */
	public Iterable<MemeInfo> getRecordsSinceLast();
	
	/*
	 * Returns memes from "Top 10" subpage
	 */
	public Iterable<MemeInfo> getTopRecords();
}