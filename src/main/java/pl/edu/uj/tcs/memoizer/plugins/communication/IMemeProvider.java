package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.ArrayList;
import java.util.List;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public interface IMemeProvider {

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
	 * Stops object - cancels all connections and pending downloads
	 */
	void cancel();
}
