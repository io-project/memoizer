package pl.edu.uj.tcs.memoizer.gui.views;

import javax.swing.JPanel;

import pl.edu.uj.tcs.memoizer.gui.models.ILegacyMemoizerModel;

/**
 * 
 * @author pkubiak
 * @author Maciej Poleski
 */
public abstract class JMemoizerView extends JPanel implements ILegacyMemoizerView {
	//abstract public JMemoizerViewPanel(ILegacyMemoizerModel model);
	public abstract void attachModel(ILegacyMemoizerModel model);
	
	public abstract void scrollToNext();
	public abstract void scrollToPrevious();
	public abstract void scrollTo(int k);

    /**
	 * Zresetuj widok i zczytaj model od nowa
	 */
	public abstract void refresh();
	
	
}

