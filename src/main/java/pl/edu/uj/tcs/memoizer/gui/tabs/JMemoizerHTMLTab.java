package pl.edu.uj.tcs.memoizer.gui.tabs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.io.IOUtils;

import pl.edu.uj.tcs.memoizer.gui.MainWindow;

public final class JMemoizerHTMLTab extends JMemoizerTab {
	private String html;
	private JPanel panel;
	
	public JMemoizerHTMLTab(String title, String htmlSource, Icon icon){
		this.html = html;
		this.icon = icon;
		this.title = title;
	}
	
	public JMemoizerHTMLTab(String title, String htmlSource){
		this(title,htmlSource, new ImageIcon(MainWindow.class.getResource("/icons/world.gif")));
	}
	

	public JMemoizerHTMLTab(String title, URL url, Icon icon){
		String html;
		try {
			html = IOUtils.toString(url.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			html = "<h1>Error</h1><p>"+e+"</p>";
		}
		this.html = html;
		this.title = title;
		this.icon = icon;
	}
	
	public JMemoizerHTMLTab(String title, URL url){
		this(title, url, new ImageIcon(MainWindow.class.getResource("/icons/world.gif")));
	}
	
	
	private void initialize(){
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		
		HTMLEditorKit kit = new HTMLEditorKit();
		editorPane.setEditorKit(kit);
		
		Document doc = kit.createDefaultDocument();
		editorPane.setDocument(doc);
		editorPane.setText(this.html);
		
		JScrollPane scrollPane = new JScrollPane(editorPane);
		
		
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		this.panel.add(scrollPane,BorderLayout.CENTER);
	}
	
	@Override
	public JPanel getViewport() {
		if(panel==null)
			initialize();
		
		return panel;
	}

}
