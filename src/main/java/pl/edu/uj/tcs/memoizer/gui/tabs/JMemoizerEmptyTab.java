package pl.edu.uj.tcs.memoizer.gui.tabs;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pl.edu.uj.tcs.memoizer.gui.MainWindow;

public final class JMemoizerEmptyTab extends JMemoizerTab {
	private JPanel panel;
	
	public JMemoizerEmptyTab(String name){
		this.title = name;
		this.icon = new ImageIcon(MainWindow.class.getResource("/icons/about.gif"));
		this.panel = new JPanel();
		
	}
	@Override
	public JPanel getViewport() {
		return this.panel;
	}

}
