package pl.edu.uj.tcs.memoizer.gui.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.gui.MainWindow;
import pl.edu.uj.tcs.memoizer.gui.models.IMemoizerModel;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class JInfinityScrollView extends JMemoizerView {
	private IMemoizerModel model;
	
	private static final Logger LOG = Logger.getLogger(MainWindow.class);

	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final Semaphore backgroundThreadIsRunning = new Semaphore(1);
	
	private int showedItems = 0;
	private JPanel panelInner;
	
	/**
	 * TODO utrzymywanie wysokości przy resizowaniu okna
	 * TODO Niewypisywać debugów na stdout
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
	
	public JInfinityScrollView(){
		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panelOutter = new ScrollablePanel();
		
		scrollPane.setViewportView(panelOutter);
		panelOutter.setLayout(new BorderLayout(0, 0));
		
		panelInner = new JPanel();
		panelOutter.add(panelInner);
		panelInner.setLayout(new BoxLayout(panelInner, BoxLayout.Y_AXIS));
		
		final JLabel lblSpinner = new JLabel("");
		lblSpinner.setPreferredSize(new Dimension(100, 600));
		lblSpinner.setBackground(Color.BLACK);
		lblSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblSpinner.setAlignmentY(Component.TOP_ALIGNMENT);
		lblSpinner.setBorder(new EmptyBorder(10, 0, 10, 0));
		lblSpinner.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSpinner.setVerticalAlignment(SwingConstants.TOP);
		panelOutter.add(lblSpinner, BorderLayout.SOUTH);
		lblSpinner.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpinner.setIcon(new ImageIcon(this.getClass().getResource("/icons/ajax-loader.gif")));
	
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				LOG.debug(scrollPane.getSize());
				lblSpinner.setPreferredSize(scrollPane.getSize());
			}
		});
		
		/* Testowa implementacja infinityScroll'a */
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			@Override
			public void adjustmentValueChanged(AdjustmentEvent ae) {
		        int extent = scrollPane.getVerticalScrollBar().getModel().getExtent();
		        
		        if(scrollPane.getVerticalScrollBar().getValue()+extent+lblSpinner.getHeight()>scrollPane.getVerticalScrollBar().getMaximum()){
		        	
		        	/**
		        	 * TODO zabezpieczyć na wypadek sigfaulta w workerze
		        	 */
		        	if(backgroundThreadIsRunning.tryAcquire()){
		        		LOG.debug("Added AsyncTask");
		        		
		        		if(model!=null){
		        			if(model.tryGet(showedItems)){
		        				notifyUpdate();
		        			}
		        		}else
		        			backgroundThreadIsRunning.release();
		        	}
		        }
		    }
		});
	}
	
	@Override
	public void attachModel(IMemoizerModel model) {
		this.model = model;
		model.bindView(this);
	}

	@Override
	public void scrollToNext() {
		// TODO Auto-generated method stub
	}

	@Override
	public void scrollToPrevious() {
		// TODO Auto-generated method stub

	}

	@Override
	public void scrollTo(int k) {
		// TODO Auto-generated method stub

	}

	@Override
	public synchronized void notifyUpdate() {
		System.out.println("NotifyUpdates()");
		if(model.tryGet(showedItems)){
			final Meme meme = model.get(showedItems);
			System.out.println(meme);
			if(meme == null){
				//TODO dodać obłsugę gdy nastąpi koniec danych
				LOG.debug("No more data here");
				
			}else{
				executorService.execute(new Runnable() {
	        	    public void run() {
		        		LOG.debug("Asynchronous task");
 
	        	        // TODO Dodać ładowanie więcej niż jednego obrazka
	        	        final JInfinityScrollViewItem imagePanel = new JInfinityScrollViewItem(meme);
	        	        showedItems++;
	        	        SwingUtilities.invokeLater(new Runnable() {
	        	            public void run() {
	        	              JInfinityScrollView.this.panelInner.add(imagePanel);
	        	              JInfinityScrollView.this.panelInner.add(new JSeparator());
	        	              JInfinityScrollView.this.panelInner.revalidate();
	        	              backgroundThreadIsRunning.release();
	        	            }
	        	          });
	        	    }
	        	});
			}
		}
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

}
