package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.ArrayList;
import java.util.List;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public interface IPluginConnector {

	/**
	 * Sets the new view for every chosen plugin.
	 * This function changes the current state of the object. 
	 * Every subsequent call of the getNextMeme method will return memes
	 * only from plugins currently chosen in the state.
	 * 
	 * @param view
	 * @param plugins
	 * @throws InvalidPlugin 
	 */
	void setView(IPluginView view, List<IDownloadPlugin> plugins) throws InvalidPlugin;
	
	/**
	 * @return current set view
	 */
	IPluginView getCurrentView();
	
	/**
	 * @return every view type that is provided by at least one plugin
	 */
	List<EViewType> getAvailableViews();
	
	/**
	 * @param viewType
	 * @return a list of plugins providing given view
	 */
	List<IDownloadPlugin> getPluginsForView(EViewType viewType);
	
	/**
	 * Resets the connector including actual state of plugins and a current view
	 */
	void resetState();
	
	/**
	 * Closes class - stops all connections and pending downloads
	 */
	void close();
}
