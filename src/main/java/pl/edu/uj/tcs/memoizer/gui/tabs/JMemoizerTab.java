package pl.edu.uj.tcs.memoizer.gui.tabs;

import javax.swing.Icon;
import javax.swing.JPanel;

public abstract class JMemoizerTab extends JPanel {
	protected String title;
	protected Icon icon;
	
	public String getTitle(){
		return this.title;
	}
	
	public Icon getIcon(){
		return this.icon;
	}
	
	public abstract JPanel getViewport();
}
