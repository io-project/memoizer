package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException;
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
	 * @throws InvalidPluginException when one of plugins does not provide given view
	 */
	void setView(IPluginView view, List<IDownloadPlugin> plugins) throws InvalidPluginException;
	
	/**
	 * @return current set view
	 */
	IPluginView getCurrentView();
	
	
	/**
	 * 
	 * @return true if there is queued meme 
	 */
	boolean hasNext();
	
	/**
	 * @return next meme according to currently set view
	 * @throws ExecutionException if one of downloading plugins throws such
	 * @throws DownloadMemeException if there are not memes to return from method
	 */
	Meme getNext() throws ExecutionException, DownloadMemeException;
	
	/**
	 * @return up to n next memes
	 * @throws ExecutionException if one of downloading plugins throws such
	 * @throws DownloadMemeException if there are not memes to return from method
	 */
	List<Meme> getNext(int n) throws ExecutionException, DownloadMemeException;
	
	/**
	 * Stops object - cancels all connections and pending downloads
	 * After execution of this method IMemeProvider may be no longer usable and in valid state
	 */
	void stop();
}
