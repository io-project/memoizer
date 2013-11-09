package pl.edu.uj.tcs.memoizer.plugins;

import java.util.List;


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
	
	public List<EViewType> getAvailableViews();
	
	public void setView(EViewType viewType);
}