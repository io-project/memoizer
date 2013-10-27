package pl.edu.uj.tcs.memoizer.plugins;


/*
 * Interfejs opisujacy pluginy do pobierania memow
 * @author pmikos (sokar92)
 */
public interface IDownloadPlugin extends IPlugin {
	
	/*
	 * Zwraca wszystkei memy od odstatniego update'a
	 */
	public Iterable<MemeInfo> getRecordsSinceLast();
}