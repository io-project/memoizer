package pl.edu.uj.tcs.memoizer.gui;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.Main;
import pl.edu.uj.tcs.memoizer.configuration.Version;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.events.exceptions.EventException;
import pl.edu.uj.tcs.memoizer.gui.tabs.JMemoizerHTMLTab;
import pl.edu.uj.tcs.memoizer.gui.tabs.JMemoizerMemeTab;
import pl.edu.uj.tcs.memoizer.gui.tabs.JMemoizerTab;
import pl.edu.uj.tcs.memoizer.gui.tabs.JAccountsTab;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.communication.IPluginManager;
import pl.edu.uj.tcs.memoizer.runtime.ShutdownEvent;
import pl.edu.uj.tcs.memoizer.serialization.StateMap;

/**
 * 
 * @author pkubiak
 */
public class MainWindow {
        private IPluginManager pluginManager;
        private JFrame frame;
        private JMenuBar menuBar;
        private JTabbedPane tabsPanel;
        private JPanel introPanel;
        private JLayeredPane layeredPane;
        private HashMap<String, JMenuItem> selectedSources = new HashMap<>();
        
        private static final Logger LOG = Logger.getLogger(Main.class);
        private StateMap config;
        private IEventService eventService; // Tylko do emisji ShutdownEvent
        private MetadataHandler metadataHandler;
        
        /**
         * Create the application.
         */
        public MainWindow(IPluginManager pluginManager, IEventService eventService, StateMap config) {
                this.pluginManager = pluginManager;
                this.config = config;
                this.eventService = eventService;
                this.metadataHandler = new MetadataHandler(config);
                
                initialize();
                this.frame.setVisible(true);
        }

        /**
         * 
         * @return List of identifiers of currently selected plugins (in menu).
         */
        private List<String> getSelectedSources(){
                ArrayList<String> list = new ArrayList<String>(); 

                for(Entry<String, JMenuItem> item: selectedSources.entrySet()){
                        if(item.getValue().isSelected()){
                                LOG.debug("SELECTED PLUGIN: "+item.getKey());
                                list.add(item.getKey());
                        }
                }
                return list;
        }
        
        private class SelectedSourcesActionListener implements ActionListener{
                private EViewType viewType;
                public SelectedSourcesActionListener(EViewType viewType){
                        this.viewType = viewType;
                }
                @Override
                public void actionPerformed(ActionEvent e) {
                        List<String> pluginsNames = MainWindow.this.getSelectedSources();
                        
                        MainWindow.this.addTab(
                                new JMemoizerMemeTab("Selected sources", viewType, pluginsNames, pluginManager, metadataHandler)
                        );
                }
        }
        
        private class SingleSourceActionListener implements ActionListener{
                private EViewType viewType;
                private String pluginName;
                public SingleSourceActionListener(String pluginName, EViewType viewType){
                        this.viewType = viewType;
                        this.pluginName = pluginName;
                }
                
                @Override
                public void actionPerformed(ActionEvent e) {
                        MainWindow.this.addTab(new JMemoizerMemeTab(viewType, pluginName, pluginManager, metadataHandler));                
                }
        }
        
        private class SelectionActionListener implements ActionListener{
                private boolean select;
                public SelectionActionListener(boolean select){
                        this.select = select;
                }
                @Override
                public void actionPerformed(ActionEvent e) {
                        for(Entry<String, JMenuItem> item: selectedSources.entrySet()){
                                item.getValue().setSelected(this.select);
                        }
                }
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
                                viewItem.setIcon(IconManager.getIconForViewType(viewType));//set icon
                                
                                //TODO dodawanie ikonek do widoków
                                item = new JMenuItem("Selected Sources");
                                item.addActionListener(new SelectedSourcesActionListener(fviewType));
                                item.setIcon(IconManager.getIconForName("items-list"));
                                
                                viewItem.add(item);
                                viewItem.add(new JSeparator());
                                
                                for(String pluginName: pluginManager.getPluginsNamesForView(viewType)){
                                        item = new JMenuItem(pluginName);
                                        item.setIcon(new ImageIcon(pluginManager.getIconForPluginName(pluginName)));

                                        item.addActionListener(new SingleSourceActionListener(pluginName, viewType));
                                        viewItem.add(item);
                                }
                                
                                viewItem.add(new JSeparator());
                                
                                item = new JMenuItem("All");
                                viewItem.add(item);
                                
                                menu.add(viewItem);
                        }
                        menuBar.add(menu);//Views
                }
                
                //Sources menu
                if(pluginManager!=null){
                        menu = new JMenu("Sources");
                        selectedSources.clear();
                        
                        //Load selected-plugins from config files
                        JSONObject obj = config.get("gui-config").getJSON();
                        JSONArray arr = null;//array of selected plugins
                        try{
                                arr = obj.getJSONArray("selected-plugins");
                        }catch(net.sf.json.JSONException e){}
                        
                        if(arr==null)
                                arr = new JSONArray();
                        
                        
                        for(String pluginName: pluginManager.getAllPluginNames()){
                                item = new JCheckBoxMenuItem(pluginName);

                                item.setIcon(new ImageIcon(pluginManager.getIconForPluginName(pluginName)));
                                item.setSelected(arr.contains(pluginName));

                                selectedSources.put(pluginName, item);
                                menu.add(item);
                        }
                        menu.add(new JSeparator());
                        
                        item = new JMenuItem("Select all");
                        item.addActionListener(new SelectionActionListener(true));
                        item.setIcon(IconManager.getIconForName("select-all"));
                        menu.add(item);
                
                        item = new JMenuItem("Unselect all");
                        item.addActionListener(new SelectionActionListener(false));
                        item.setIcon(IconManager.getIconForName("unselect-all"));
                        menu.add(item);
                        
                        menuBar.add(menu);
                }
                
                //More menu
                //TODO dodać actionListener do przycisków 
                menu = new JMenu("More");
                
                item = new JMenuItem("Settings");
                item.setIcon(IconManager.getIconForName("settings"));
                menu.add(item);
                
                item = new JMenuItem("Plugins");
                item.setIcon(IconManager.getIconForName("plugin-manager"));
                menu.add(item);
                
                item = new JMenuItem("Help");
                item.setIcon(IconManager.getIconForName("help"));
                item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                //Show help page
                                MainWindow.this.addTab(new JMemoizerHTMLTab("Help", MainWindow.class.getResource("/html/help/index.html"), IconManager.getIconForName("about")));
                        }
                });
                menu.add(item);
                
                item = new JMenuItem("About");
                item.setIcon(IconManager.getIconForName("about"));
                item.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                                //Show about page
                                MainWindow.this.addTab(new JMemoizerHTMLTab("About", MainWindow.class.getResource("/html/about/index.html"), IconManager.getIconForName("about")));
                        }
                });
                menu.add(item);        

				item = new JMenuItem("Account");
				item.setIcon(new ImageIcon(MainWindow.class.getResource("/icons/accounts.gif")));
				item.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						MainWindow.this.addTab(new JAccountsTab("AccountsSettings", new ImageIcon(MainWindow.class.getResource("/icons/accounts.gif")), config));
						
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
            btnClose.setRolloverIcon(IconManager.getIconForName("tab-close-on"));
            btnClose.setRolloverEnabled(true);
            btnClose.setIcon(IconManager.getIconForName("tab-close-off"));

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
            pnlTab.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
            
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
                frame.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent e)
                    {
                        JFrame frame = (JFrame)e.getSource();
                        JSONObject x = MainWindow.this.config.get("gui-config").getJSON();
                        JSONArray a = new JSONArray();
                        for(String plugin: getSelectedSources())
                                a.add(plugin);
                        
                        x.put("selected-plugins", a);
                        LOG.debug("JSON: "+x.toString());
                        
                        try {
                                //TODO problem z kolejnością eventów => event powinien killować app
                                        MainWindow.this.eventService.call(new ShutdownEvent());
                                        
                                } catch (EventException e1) {
                                        e1.printStackTrace();
                                }
                    }
                });

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
                
                JLabel introLabel = new JLabel("Memoizer v."+Version.VERSION);
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
