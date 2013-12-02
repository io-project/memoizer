package pl.edu.uj.tcs.memoizer.gui.views;

import javax.swing.JPanel;

import pl.edu.uj.tcs.memoizer.gui.models.IMemoizerModel;

/**
 * 
 * @author pkubiak
 */
public abstract class JMemoizerView extends JPanel{
	//abstract public JMemoizerViewPanel(IMemoizerModel model);
	public abstract void attachModel(IMemoizerModel model);
	
	public abstract void scrollToNext();
	public abstract void scrollToPrevious();
	public abstract void scrollTo(int k);
	
	public abstract void notifyUpdate();
	public abstract void notifyStreamEnd();
	
	/**
	 * Zresetuj widok i zczytaj model od nowa
	 */
	public abstract void refresh();
	
	
}

