package pl.edu.uj.tcs.memoizer.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.gui.models.IMemoizerModel;
import pl.edu.uj.tcs.memoizer.gui.models.SimpleMemoizerModel;
import pl.edu.uj.tcs.memoizer.gui.views.JInfinityScrollView;
import pl.edu.uj.tcs.memoizer.gui.views.JInfinityScrollViewItem;
import pl.edu.uj.tcs.memoizer.gui.views.JMemoizerView;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.communication.MemeProvider;
import pl.edu.uj.tcs.memoizer.plugins.communication.PluginManager;


public class MainWindow {
	
	
	
	public void setVisible(boolean isVisible) {
		frmMemoizer.setVisible(isVisible);
	}
	
	private JFrame frmMemoizer;
	
	MemeProvider memeProvider = null;
	PluginManager pluginManager = null;
	SimpleMemoizerModel model;
	
	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	public MainWindow(PluginManager pluginManager){
		this.pluginManager = pluginManager;
		this.model = new SimpleMemoizerModel(memeProvider, pluginManager);
		
		this.memeProvider = new MemeProvider(pluginManager.eventService, this.model);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMemoizer = new JFrame();
		frmMemoizer.setSize(new Dimension(700,500));
		JMemoizerView viewPanel = new JInfinityScrollView();
		this.model.bindView(viewPanel);
		viewPanel.attachModel(this.model);
		
		frmMemoizer.getContentPane().add(viewPanel, BorderLayout.CENTER);
		
		/* Przykładowe Menu */
		JMenuBar menuBar = new JMenuBar();
		frmMemoizer.setJMenuBar(menuBar);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		for(EViewType viewType: pluginManager.getAvailableViews()){
			JMenu menu = new JMenu(viewType.getName());
			mnView.add(menu);
			
			JMenuItem mntmSelectedSources = new JMenuItem("Selected Sources");
			menu.add(mntmSelectedSources);
			JSeparator separator = new JSeparator();
			menu.add(separator);
			for(IPlugin plugin: pluginManager.getPluginsForView(viewType)){
				JMenuItem x = new JMenuItem(plugin.getName());
				menu.add(x);				
			}
		}
		
		JMenu mnNewMenu = new JMenu("Sources");
		menuBar.add(mnNewMenu);
		
		String[] providers = {};//"Demotywatory", "Kwej", "Fabryka memów", "Pewex.pl", "Kotburger", "Student Potrafi"};
		for(IPlugin plugin: pluginManager.getLoadedPlugins()){
			String label = plugin.getName();

			JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem(label);
			mnNewMenu.add(checkbox);
			
//			JMenuItem x = new JMenuItem(label);mnByDate.add(x);
//			x = new JMenuItem(label);mnTop.add(x);
//			x = new JMenuItem(label);mnSearch.add(x);
//			x = new JMenuItem(label);mnFavourites.add(x);
		}
		
		JMenu mnAbout = new JMenu("More");
		menuBar.add(mnAbout);
		
		JMenuItem mntmConfig = new JMenuItem("Preferences");
		mnAbout.add(mntmConfig);
		
		JMenuItem mntmReloadPlugins = new JMenuItem("Reload plugins");
		mnAbout.add(mntmReloadPlugins);
		
		JSeparator separator_1 = new JSeparator();
		mnAbout.add(separator_1);
		
		JMenuItem mntmAbout = new JMenuItem("About");
		mnAbout.add(mntmAbout);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		menuBar.add(horizontalGlue);
		
//		JButton buttonPrevImage = new JButton("«");
//		buttonPrevImage.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ev) {
//				/** 
//				 * TODO add TreeLock more at javadoc
//				 * TODO Lepsze sprawdzanie IndexOutOfBoundException zamiast try/catch
//				 * TODO Sprawdzić w czy "get nth component" nie działa w czasie O(n) 
//				 */
//				int a = -1, b = panelInner.getComponentCount()-1;
//				int Y = scrollPane.getVerticalScrollBar().getValue();
//				//Szukamy największego n takiego ze Y(nth component)<Y;
//				while(a<b){
//					int s = (a+b+1)/2;
//					if(panelInner.getComponent(s).getLocation().y>=Y)
//						b = s-1;
//					else
//						a = s;
//	        		LOG.debug(a + " " + b);
//				}
//				
//				try{
//					while(a>=0&&panelInner.getComponent(a).getClass()!=JInfinityScrollViewItem.class)
//						a--;
//					if(a<0)a=0;
//					
//					LOG.debug("BinarySearch result: "+a+" -> "+panelInner.getComponent(a).getClass());
//					//panelInner.getComponent(a).setBackground(Color.BLACK);
//					scrollPane.getVerticalScrollBar().setValue(panelInner.getComponent(a).getLocation().y);//scroll to next image
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
//		});
//		
//		buttonPrevImage.setMargin(new Insets(0, 8, 0, 8));
//		buttonPrevImage.setFont(new Font("Dialog", Font.BOLD, 20));
//		buttonPrevImage.setHorizontalAlignment(SwingConstants.RIGHT);
//		menuBar.add(buttonPrevImage);
//		
//		
//		JButton buttonNextImage = new JButton("»");
//		buttonNextImage.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent ev) {
//				/**
//				 * TODO add TreeLock more at javadoc 
//				 * TODO Dodać jakieś lepsze sprawdzanie IndexOutOfBoundException zamiast try
//				 * TODO Rozwiązać problem z tym gdy dochodzimy do konca strony i nie można bardziej zescrollować strony
//				 * TODO Sprawdzić w czy "get nth component" nie działa w czasie O(n)
//				 */
//				int a = 0, b = panelInner.getComponentCount();
//				int Y = scrollPane.getVerticalScrollBar().getValue();
//				//Szukamy najmniejszego n takiego ze Y(nth component)>Y;
//				while(a<b){
//					int s = (a+b)/2;
//					if(panelInner.getComponent(s).getLocation().y<=Y)
//						a = s+1;
//					else
//						b = s;
//					LOG.debug(a+" "+b);
//				}
//				
//				try{
//					while(a<panelInner.getComponentCount()&&panelInner.getComponent(a).getClass()!=JInfinityScrollViewItem.class)
//						a++;
//					if(a>=panelInner.getComponentCount())
//						a=panelInner.getComponentCount()-1;
//					
//					LOG.debug("BinarySearch result: "+a+" -> "+panelInner.getComponent(a).getClass());
//					//panelInner.getComponent(a).setBackground(Color.BLACK);
//					scrollPane.getVerticalScrollBar().setValue(panelInner.getComponent(a).getLocation().y);//scroll to next image
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//			}
//		});
//		
//		buttonNextImage.setMargin(new Insets(0, 8, 0, 8));
//		buttonNextImage.setFont(new Font("Dialog", Font.BOLD, 20));
//		menuBar.add(buttonNextImage);
	}

}
