package pl.edu.uj.tcs.memoizer.gui.tmp;
//import java.awt.EventQueue;
//
//import javax.imageio.ImageIO;
//import javax.swing.JFrame;
//import javax.swing.JScrollPane;
//
//import java.awt.BorderLayout;
//
//import javax.swing.ImageIcon;
//import javax.swing.ScrollPaneConstants;
//import javax.swing.JPanel;
//import javax.swing.BoxLayout;
//import javax.swing.JLabel;
//import javax.swing.UIManager;
//
//import java.awt.GridLayout;
//
//import net.miginfocom.swing.MigLayout;
//
//import java.awt.Canvas;
//
//import javax.swing.JToggleButton;
//
//import java.awt.Font;
//import java.awt.event.AdjustmentEvent;
//import java.awt.event.AdjustmentListener;
//import java.awt.event.ComponentAdapter;
//import java.awt.event.ComponentEvent;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import javax.swing.JInternalFrame;
//import javax.swing.JSeparator;
//import javax.swing.JProgressBar;
//import javax.swing.JMenuBar;
//import javax.swing.JMenu;
//import javax.swing.SwingConstants;
//import javax.swing.JTextPane;
//import java.awt.SystemColor;
//import javax.swing.JCheckBoxMenuItem;
//import javax.swing.JMenuItem;
//import java.awt.FlowLayout;
//import javax.swing.Box;
//import javax.swing.JButton;
//import java.awt.Component;
//import javax.swing.SpringLayout;
//
//public class ImagesList_old {
//
//	private JFrame frame;
//
//	/**
//	 * Launch the application.
//	 */
//	/*public static void main(String[] args) {
//		
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					
//			        //UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//			        
//					ImagesList_old window = new ImagesList_old();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}*/
//
//	/**
//	 * Create the application.
//	 */
//	public ImagesList_old() {
//		initialize();
//	}
//
//	
//	
//	/**
//	 * Initialize the contents of the frame.
//	 */
//	private void initialize() {
//		
//		
//		frame = new JFrame();
//		frame.setBounds(100, 100, 555, 578);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
//	    
//		/*String path = "http://retro.pewex.pl/uimages/services/pewex/i18n/pl_PL/201310/1382521263_by_krzys_500.jpg?1382521276";
//		URL url = new URL(path);
//		BufferedImage image = ImageIO.read(url);*/
//		
//		final JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		frame.getContentPane().add(scrollPane);
//
//		
//		JPanel panel = new JPanel();
//		
//		JButton button = new JButton();
//		panel.add(button);
//		//frame.getContentPane().add(panel);
//		
//		for(int i=0;i<4;i++){
//			JImagePanel imagePanel = new JImagePanel("Jeden plakat, MILION wspomnień!", "Kot Tip Top, Rożnowa Pantera, Smerfetka, Kosmiczny Duch, Batman, Janet, Scooby Doo, Muttley, Kudłaty, Bałwan, Dinocco, Goryl Magilla, Pan Jinks, Flinston, Willma Flinston, Hong Kong Phooey, Elroy, Joe Jetson, George Jetson, Becky, TOM, Papa Smerf, Astro, Barney, Pebbles, Bart, Gargamel, Bambam, JERRY!, Wredniak, Johnny Quest, Pixie, Miś Yogi, Wally Gator, Boo Boo, Dixie, Judy i Pies Huckleberry", "http://retro.pewex.pl/uimages/services/pewex/i18n/pl_PL/201310/1382521263_by_krzys_500.jpg?1382521276", "http://retro.pewex.pl/477976/Jeden-plakat-MILION-wspomnien", "http://retro.pewex.pl/477976/Jeden-plakat-MILION-wspomnien");
//			panel.add(imagePanel);
//		}
//		
//		//panel.setSize(200, 1000);
//		
//		panel.revalidate();
//		scrollPane.add(panel);
//		panel.setLayout(new MigLayout("fillx", "[grow]", "[][]"));
//		scrollPane.revalidate();
//		
//		/*scrollPane.validate();
//		scrollPane.revalidate();
//		
//		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
//			@Override
//			public void adjustmentValueChanged(AdjustmentEvent ae) {
//		        int extent = scrollPane.getVerticalScrollBar().getModel().getExtent();
//		        System.out.println("Value: " + (scrollPane.getVerticalScrollBar().getValue()+extent) + " Max: " + scrollPane.getVerticalScrollBar().getMaximum());
//		        
//		        if(scrollPane.getVerticalScrollBar().getValue()+extent==scrollPane.getVerticalScrollBar().getMaximum()){
//		        	System.out.println("Need new Image");
//		        	
//		        }
//		    }
//			
//		});*/
//		
//		
//		/*JMenuBar menuBar = new JMenuBar();
//		frame.setJMenuBar(menuBar);
//		
//		JMenu mnNewMenu = new JMenu("Sources");
//		menuBar.add(mnNewMenu);
//		
//		JCheckBoxMenuItem chckbxmntmDemotywatory = new JCheckBoxMenuItem("Demotywatory");
//		mnNewMenu.add(chckbxmntmDemotywatory);
//		
//		JCheckBoxMenuItem chckbxmntmKwejk = new JCheckBoxMenuItem("Kwejk");
//		mnNewMenu.add(chckbxmntmKwejk);
//		
//		JCheckBoxMenuItem chckbxmntmFabrykaMemw = new JCheckBoxMenuItem("Fabryka memów");
//		mnNewMenu.add(chckbxmntmFabrykaMemw);
//		
//		JCheckBoxMenuItem chckbxmntmPewexpl = new JCheckBoxMenuItem("Pewex.pl");
//		mnNewMenu.add(chckbxmntmPewexpl);
//		
//		JMenu mnAbout = new JMenu("More");
//		menuBar.add(mnAbout);
//		
//		JMenuItem mntmConfig = new JMenuItem("Preferences");
//		mnAbout.add(mntmConfig);
//		
//		JMenuItem mntmReloadPlugins = new JMenuItem("Reload plugins");
//		mnAbout.add(mntmReloadPlugins);
//		
//		JSeparator separator_1 = new JSeparator();
//		mnAbout.add(separator_1);
//		
//		JMenuItem mntmAbout = new JMenuItem("About");
//		mnAbout.add(mntmAbout);*/
//
//	  
//		
//	}
//}
