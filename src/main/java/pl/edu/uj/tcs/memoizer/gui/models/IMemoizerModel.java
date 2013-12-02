package pl.edu.uj.tcs.memoizer.gui.models;

import java.util.concurrent.Future;

import pl.edu.uj.tcs.memoizer.gui.views.JMemoizerView;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.plugins.communication.DownloadMemeException;

/**
 * @author pkubiak
 */
public interface IMemoizerModel{
	/**
	 * Bind view for future notification.
	 * @param view
	 */
	public void bindView(JMemoizerView view);

	/**
	 * Check if k'th element of models is available, if not acquire it and notify view
	 * @param k
	 * @return
	 */
	public boolean tryGet(int k);

	/**
	 * Blocking request for k'th element of model, if it is already available
	 * it will be returned immediately otherwise thread will block until it
	 * @param k
	 * @return k'th element of model
	 */
	public Meme get(int k) throws DownloadMemeException;
}
