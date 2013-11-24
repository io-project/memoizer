package pl.edu.uj.tcs.memoizer.gui;


import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JLayeredPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;

import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.JPanel;

import javax.swing.JTabbedPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JSeparator;

import pl.edu.uj.tcs.memoizer.gui.tabs.JMemoizerHTMLTab;
import pl.edu.uj.tcs.memoizer.gui.tabs.JMemoizerMemeTab;
import pl.edu.uj.tcs.memoizer.gui.tabs.JMemoizerTab;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.communication.PluginManager;

public class MainWindow {
	private PluginManager pluginManager;
	private JFrame frame;
	private JMenuBar menuBar;
	private JTabbedPane tabsPanel;
	private JPanel introPanel;
	private JLayeredPane layeredPane;
	
	/**
	 * Create the application.
	 */
	public MainWindow(PluginManager pluginManager) {
		this.pluginManager = pluginManager;
		initialize();
		this.frame.setVisible(true);
	}

	private void generateMenu(){
		//TODO podpiąć resztę actionListener
		JMenu menu;
		JMenuItem item;

		//clear menu
		menuBar.removeAll();
		
		//Views menu		
		if(pluginManager!=null){
			menu = new JMenu("View");
			for(EViewType viewType: pluginManager.getAvailableViews()){
				final EViewType fviewType = viewType;
				
				JMenu viewItem = new JMenu(viewType.getName());
				//TODO dodawanie ikonek do widoków
				item = new JMenuItem("Selected Sources");
				//TODO podpiąć actionListenery do Selected Sources
				viewItem.add(item);
				viewItem.add(new JSeparator());
				
				for(IDownloadPlugin plugin: pluginManager.getPluginsForView(viewType)){
					item = new JMenuItem(plugin.getName());
					//TODO dodawanie ikonek do pluginów
					//TODO podpiąć actionListenery do zmian pluginów
					final IDownloadPlugin fplugin = plugin;
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							//MainWindow.this.pluginManager.
							MainWindow.this.addTab(new JMemoizerMemeTab(fviewType, fplugin));
						}
					});
					viewItem.add(item);
					
				}
				menu.add(viewItem);
			}
			menuBar.add(menu);//Views
		}
		
		//Sources menu
		if(pluginManager!=null){
			menu = new JMenu("Sources");
			
			for(IPlugin plugin: pluginManager.getLoadedPlugins()){
				item = new JMenuItem(plugin.getName());
				//TODO dodawanie ikonek do pluginów
				//TODO podpiąć actionListenery do zmian pluginów
				menu.add(item);
			}
			menu.add(new JSeparator());
			
			item = new JMenuItem("Select all");
			menu.add(item);
		
			item = new JMenuItem("Deselect all");
			menu.add(item);
			
			menuBar.add(menu);
		}
		
		//More menu
		//TODO dodać actionListener do przycisków 
		menu = new JMenu("More");
		
		item = new JMenuItem("Settings");
		item.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/settings.gif")));
		menu.add(item);
		
		item = new JMenuItem("Plugins");
		item.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/package.gif")));
		menu.add(item);
		
		item = new JMenuItem("Help");
		item.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/help.gif")));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Show help page
				MainWindow.this.addTab(new JMemoizerHTMLTab("Help", MainWindow.class.getResource("/html/help/index.html"), new ImageIcon(MainWindow.class.getResource("/icons/help.gif"))));
			}
		});
		menu.add(item);
		
		item = new JMenuItem("About");
		item.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/about.gif")));
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Show about page
				MainWindow.this.addTab(new JMemoizerHTMLTab("About", MainWindow.class.getResource("/html/about/index.html"), new ImageIcon(MainWindow.class.getResource("/icons/about.gif"))));
			}
		});
		menu.add(item);
		menuBar.add(menu);
	}
	
	/**
	 * Tab close button generation based on: http://paperjammed.com/2012/11/22/adding-tab-close-buttons-to-a-jtabbedpane-in-java-swing/
	 * @param tab
	 */
	private void addTab(final JMemoizerTab tab){
		//TODO dodać tworzenie/ładowanie kart w nowym wątku, tak aby nie zacinało to całego GUI
		
		tabsPanel.addTab(null, tab.getViewport());

		int pos = tabsPanel.indexOfComponent(tab.getViewport());
		
		// Create a FlowLayout that will space things 5px apart
	    FlowLayout f = new FlowLayout(FlowLayout.CENTER, 5, 0);

	    // Make a small JPanel with the layout and make it non-opaque
	    JPanel pnlTab = new JPanel(f);
	 
	    pnlTab.setOpaque(false);

	    // Add a JLabel with title and the left-side tab icon
	    JLabel lblTitle = new JLabel(tab.getTitle());
	    lblTitle.setIcon(tab.getIcon());

	    // Create a JButton for the close tab button
	    JButton btnClose = new JButton();
	    btnClose.setOpaque(false);
	    btnClose.setBackground(null);

	    // Configure icon and rollover icon for button
	    btnClose.setRolloverIcon(new ImageIcon(MainWindow.class.getResource("/icons/tabclosehover.png")));
	    btnClose.setRolloverEnabled(true);
	    btnClose.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/tabclose.png")));

	    // Set border null so the button doesn't make the tab too big
	    btnClose.setBorder(null);

	    // Make sure the button can't get focus, otherwise it looks funny
	    btnClose.setFocusable(false);
	    btnClose.setContentAreaFilled(false);

	    // Put the panel together
	    pnlTab.add(lblTitle);
	    //pnlTab.add(Box.createHorizontalGlue())
	    pnlTab.add(btnClose);

	    // Add a thin border to keep the image below the top edge of the tab
	    // when the tab is selected
	    //TODO dodać jakieś sensowne pole wokoł zamykacza
	    pnlTab.setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 2));
	    
	    tabsPanel.setTabComponentAt(pos, pnlTab);
	    
	    
	    // Add the listener that removes the tab
	    ActionListener listener = new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent e) {
	        // The component parameter must be declared "final" so that it can be
	        // referenced in the anonymous listener class like this.
	        tabsPanel.remove(tab.getViewport());
	      }
	    };
	    btnClose.addActionListener(listener);
	    
	    // Optionally bring the new tab to the front
	    tabsPanel.setSelectedComponent(tab.getViewport());
	    
	    //-------------------------------------------------------------
	    // Bonus: Adding a <Ctrl-W> keystroke binding to close the tab
	    //-------------------------------------------------------------
	    AbstractAction closeTabAction = new AbstractAction() {
	      @Override
	      public void actionPerformed(ActionEvent e) {
	        tabsPanel.remove(tab.getViewport());
	      }
	    };

	    // Create a keystroke
	    KeyStroke controlW = KeyStroke.getKeyStroke("control W");

	    // Get the appropriate input map using the JComponent constants.
	    // This one works well when the component is a container. 
	    InputMap inputMap = tab.getViewport().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

	    // Add the key binding for the keystroke to the action name
	    inputMap.put(controlW, "closeTab");

	    // Now add a single binding for the action name to the anonymous action
	    tab.getViewport().getActionMap().put("closeTab", closeTabAction);
	    
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 586, 449);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		//initialize menu
		generateMenu();
		
		layeredPane = new JLayeredPane();
		frame.getContentPane().add(layeredPane, BorderLayout.CENTER);
		layeredPane.setLayout(null);
		layeredPane.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				MainWindow.this.introPanel.setSize(MainWindow.this.layeredPane.getSize());
				MainWindow.this.tabsPanel.setSize(MainWindow.this.layeredPane.getSize());
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				MainWindow.this.introPanel.setSize(MainWindow.this.layeredPane.getSize());
				MainWindow.this.tabsPanel.setSize(MainWindow.this.layeredPane.getSize());
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		//Set panel displayed in background of application
		introPanel = new JPanel();
		introPanel.setBounds(0, 0, 600, 400);
		layeredPane.add(introPanel);
		introPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel introLabel = new JLabel("Memoizer v.0.1");
		introPanel.add(introLabel);
		introLabel.setForeground(UIManager.getColor("Label.disabledForeground"));
		introLabel.setFont(new Font("Dialog", Font.BOLD, 28));
		introLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		//Set panel with tabs
		tabsPanel = new JTabbedPane(JTabbedPane.TOP);
		layeredPane.setLayer(tabsPanel, 1);
		tabsPanel.setBounds(0, 0, 600, 400);
		
		tabsPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);//TODO it's HACK, couse tabs label doesn't scale properly
		
		layeredPane.add(tabsPanel);
		
		//Add test tabs
	}
}
