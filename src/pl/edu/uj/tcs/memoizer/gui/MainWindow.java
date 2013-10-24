package pl.edu.uj.tcs.memoizer.gui;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.Component;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.swing.JButton;
import javax.swing.Box;

import java.awt.Font;

import java.awt.Insets;


public class MainWindow {
	
	/**
	 * TODO utrzymywanie wysokości przy resizowaniu okna
	 * 
	 */
	@SuppressWarnings("serial")
	private class ScrollablePanel extends JPanel implements Scrollable{
	    public Dimension getPreferredScrollableViewportSize() {
	        return super.getPreferredSize(); //tell the JScrollPane that we want to be our 'preferredSize' - but later, we'll say that vertically, it should scroll.
	    }

	    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
	        return 16;//set to 16 because that's what you had in your code.
	    }

	    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
	        return 16;//set to 16 because that's what you had set in your code.
	    }

	    public boolean getScrollableTracksViewportWidth() {
	        return true;//track the width, and re-size as needed.
	    }

	    public boolean getScrollableTracksViewportHeight() {
	        return false; //we don't want to track the height, because we want to scroll vertically.
	    }
	};
	
	private JFrame frmMemoizer;
	ExecutorService executorService = Executors.newSingleThreadExecutor();
	final Semaphore backgroundThreadIsRunning = new Semaphore(1);
	ContentProvider contentProvider = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow(new OfflineContentProvider());
					window.frmMemoizer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	public MainWindow(ContentProvider provider){
		this.contentProvider = provider;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmMemoizer = new JFrame();
		frmMemoizer.setTitle("Memoizer");
		frmMemoizer.setBounds(100, 100, 600, 500);
		frmMemoizer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		frmMemoizer.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel panelOutter = new ScrollablePanel();
		
		scrollPane.setViewportView(panelOutter);
		panelOutter.setLayout(new BorderLayout(0, 0));
		
		final JPanel panelInner = new JPanel();
		panelOutter.add(panelInner);
		panelInner.setLayout(new BoxLayout(panelInner, BoxLayout.Y_AXIS));
		
		JLabel lblSpinner = new JLabel("");
		lblSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblSpinner.setBorder(new EmptyBorder(10, 0, 10, 0));
		lblSpinner.setHorizontalTextPosition(SwingConstants.CENTER);
		panelOutter.add(lblSpinner, BorderLayout.SOUTH);
		lblSpinner.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpinner.setIcon(new ImageIcon(this.getClass().getResource("/resources/ajax-loader.gif")));
		
		for(int i=0;i<1;i++){
			JImagePanel imagePanel = new JImagePanel();
			panelInner.add(imagePanel);
			
			JSeparator x = new JSeparator();
			panelInner.add(x);
		}
		
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			@Override
			public void adjustmentValueChanged(AdjustmentEvent ae) {
		        int extent = scrollPane.getVerticalScrollBar().getModel().getExtent();
		        
		        if(scrollPane.getVerticalScrollBar().getValue()+extent+40>scrollPane.getVerticalScrollBar().getMaximum()){
		        	
		        	/**
		        	 * TODO zabezpieczyć na wypadek sigfaulta w workerze
		        	 */
		        	if(backgroundThreadIsRunning.tryAcquire()){
		        		System.out.println("Added AsyncTask");
		        		
		        		if(contentProvider!=null){
		        			final Content contentToShow = contentProvider.getNext();
		        			if(contentToShow!=null){
					        	executorService.execute(new Runnable() {
					        	    public void run() {
					        	        System.out.println("Asynchronous task");
					        	        final JImagePanel imagePanel = new JImagePanel(contentToShow);
					        	       
		
					        	        SwingUtilities.invokeLater(new Runnable() {
					        	            public void run() {
					        	              panelInner.add(imagePanel);
					        	              panelInner.add(new JSeparator());
					        	              panelInner.revalidate();
					        	              backgroundThreadIsRunning.release();
					        	            }
					        	          });
					        	    }
					        	});
		        			}else
		        				backgroundThreadIsRunning.release();
		        		}else
		        			backgroundThreadIsRunning.release();
		        	}
		        }
		    }
		});
		
		/* Przykładowe Menu */
		JMenuBar menuBar = new JMenuBar();
		frmMemoizer.setJMenuBar(menuBar);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenu mnByDate = new JMenu("By Date");
		mnView.add(mnByDate);
		
		JMenuItem mntmSelectedSources = new JMenuItem("Selected Sources");
		mnByDate.add(mntmSelectedSources);
		
		JSeparator separator = new JSeparator();
		mnByDate.add(separator);
		
		JMenu mnTop = new JMenu("Top");
		mnView.add(mnTop);
		
		JMenuItem mntmSelectedSources_1 = new JMenuItem("Selected Sources");
		mnTop.add(mntmSelectedSources_1);
		
		JSeparator separator_2 = new JSeparator();
		mnTop.add(separator_2);
		
		JMenu mnSearch = new JMenu("Search");
		mnView.add(mnSearch);
		
		JMenuItem mntmSelectedSources_2 = new JMenuItem("Selected Sources");
		mnSearch.add(mntmSelectedSources_2);
		
		JSeparator separator_3 = new JSeparator();
		mnSearch.add(separator_3);
		
		JMenuItem mntmGoogleImages = new JMenuItem("Google Images");
		mnSearch.add(mntmGoogleImages);
		
		JMenu mnFavourites = new JMenu("Favourites");
		mnView.add(mnFavourites);
		
		JMenuItem mntmSelectedSources_3 = new JMenuItem("Selected Sources");
		mnFavourites.add(mntmSelectedSources_3);
		
		JSeparator separator_4 = new JSeparator();
		mnFavourites.add(separator_4);
		
		JMenu mnNewMenu = new JMenu("Sources");
		menuBar.add(mnNewMenu);
		
		String[] providers = {"Demotywatory", "Kwej", "Fabryka memów", "Pewex.pl", "Kotburger", "Student Potrafi"};
		for(String label: providers){
			JCheckBoxMenuItem checkbox = new JCheckBoxMenuItem(label);
			mnNewMenu.add(checkbox);
			
			JMenuItem x = new JMenuItem(label);mnByDate.add(x);
			x = new JMenuItem(label);mnTop.add(x);
			x = new JMenuItem(label);mnSearch.add(x);
			x = new JMenuItem(label);mnFavourites.add(x);
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
		
		JButton button = new JButton("«");
		button.setMargin(new Insets(0, 8, 0, 8));
		button.setFont(new Font("Dialog", Font.BOLD, 20));
		button.setHorizontalAlignment(SwingConstants.RIGHT);
		menuBar.add(button);
		
		JButton button_1 = new JButton("»");
		button_1.setMargin(new Insets(0, 8, 0, 8));
		button_1.setFont(new Font("Dialog", Font.BOLD, 20));
		menuBar.add(button_1);
	}

}
