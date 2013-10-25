package pl.edu.uj.tcs.memoizer.plugins;

import java.lang.*;

/*
 * Interfejs opisujacy pluginy do pobierania memow
 * @author pmikos (sokar92)
 */
public interface IDownloadPlugin extends IPlugin {
	
	/*
	 * Returns all records since last visit on page
	 */
	public Iterable<Record> getRecordsSinceLast();
}