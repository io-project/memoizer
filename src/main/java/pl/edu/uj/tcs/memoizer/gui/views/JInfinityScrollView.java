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
import pl.edu.uj.tcs.memoizer.gui.MetadataHandler;
import pl.edu.uj.tcs.memoizer.gui.models.ILegacyMemoizerModel;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

/**
 * @author Paweł Kubiak
 * @author Maciej Poleski
 */
public class JInfinityScrollView extends JMemoizerView {
	private ILegacyMemoizerModel model;
	
	private static final Logger LOG = Logger.getLogger(MainWindow.class);

	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	private final Semaphore backgroundThreadIsRunning = new Semaphore(1);
	
	private int showedItems = 0;
	private JPanel panelInner;
	private JScrollPane scrollPane;
	private JLabel lblSpinner;
	private JLabel lblEnd;
	private MetadataHandler metadata;
	
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
	
	public JInfinityScrollView(MetadataHandler metadata){
		this.metadata = metadata;
		scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panelOutter = new ScrollablePanel();
		
		scrollPane.setViewportView(panelOutter);
		panelOutter.setLayout(new BorderLayout(0, 0));
		
		panelInner = new JPanel();
		panelOutter.add(panelInner);
		panelInner.setLayout(new BoxLayout(panelInner, BoxLayout.Y_AXIS));
		
		
		lblSpinner = new JLabel("");
		lblSpinner.setPreferredSize(new Dimension(100, 600));
		lblSpinner.setBackground(Color.BLACK);
		lblSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblSpinner.setAlignmentY(Component.TOP_ALIGNMENT);
		lblSpinner.setBorder(new EmptyBorder(10, 0, 10, 0));
		lblSpinner.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSpinner.setVerticalAlignment(SwingConstants.TOP);
		
		lblSpinner.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpinner.setIcon(new ImageIcon(this.getClass().getResource("/ajax-loader.gif")));
		lblSpinner.setVisible(false);//create but dont show
		
		
		lblEnd = new JLabel("There is no more memes. Click to try again !");
		lblEnd.setVisible(true);
		
		JPanel notificationsPanel = new JPanel();
		notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.Y_AXIS));
		
		notificationsPanel.add(lblSpinner);
		notificationsPanel.add(lblEnd);
		panelOutter.add(notificationsPanel, BorderLayout.SOUTH);
		
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				LOG.debug(scrollPane.getSize());
				lblSpinner.setPreferredSize(scrollPane.getSize());
			}
		});
		
		/* TODO Testowa implementacja infinityScroll'a */
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			@Override
			public void adjustmentValueChanged(AdjustmentEvent ae) {
		        int extent = scrollPane.getVerticalScrollBar().getModel().getExtent();
		        
		        if(model!=null&&scrollPane.getVerticalScrollBar().getValue()+extent+lblSpinner.getHeight()>scrollPane.getVerticalScrollBar().getMaximum()){
		        	requestNext();
		        }
		    }
		});
	}
	
	private void requestNext(){
		/**
    	 * TODO zabezpieczyć na wypadek sigfaulta w workerze
    	 * TODO synchronizacja
    	 */
		lblSpinner.setVisible(true);
		lblEnd.setVisible(false);
		
		if(model!=null&&backgroundThreadIsRunning.tryAcquire()){
    		LOG.debug("Added AsyncTask");
    			
			if(model.tryGet(showedItems)){
				notifyUpdate();
			}
    	}
	}
	
	@Override
	public void attachModel(ILegacyMemoizerModel model) {
		//unbind old model
		if(this.model!=null)
			this.model.bindView(null);
		this.model = model;
		
		if(model!=null)
			model.bindView(this);
		thereIsNoModel();
	}

	@Override
	public void scrollToNext() {
		System.out.println("scrollToNext");
		 /**
         * TODO add TreeLock more at javadoc 
         * TODO Dodać jakieś lepsze sprawdzanie IndexOutOfBoundException zamiast try
         * TODO Rozwiązać problem z tym gdy dochodzimy do konca strony i nie można bardziej zescrollować strony
         * TODO Sprawdzić w czy "get nth component" nie działa w czasie O(n)
         */
        int a = 0, b = panelInner.getComponentCount();
        int Y = scrollPane.getVerticalScrollBar().getValue();
        //Szukamy najmniejszego n takiego ze Y(nth component)>Y;
        while(a<b){
            int s = (a+b)/2;
            if(panelInner.getComponent(s).getLocation().y<=Y)
                    a = s+1;
            else
                    b = s;
            System.out.println(a+" "+b);
        }
        
        try{
    		int pos = 0;
    		while(a<panelInner.getComponentCount()&&panelInner.getComponent(a).getClass()!=JInfinityScrollViewItem.class)
                a++;
    		
    		if(a==panelInner.getComponentCount())
    			pos = panelInner.getHeight();
    		else
	            pos = panelInner.getComponent(a).getLocation().y;
            
            scrollPane.getVerticalScrollBar().setValue(pos);//scroll to next image
        }catch(Exception e){
                e.printStackTrace();
        }
	}

	@Override
	public void scrollToPrevious() {
		/** 
         * TODO add TreeLock more at javadoc
         * TODO Lepsze sprawdzanie IndexOutOfBoundException zamiast try/catch
         * TODO Sprawdzić w czy "get nth component" nie działa w czasie O(n) 
         */
        int a = -1, b = panelInner.getComponentCount()-1;
        int Y = scrollPane.getVerticalScrollBar().getValue();
        //Szukamy największego n takiego ze Y(nth component)<Y;
        while(a<b){
                int s = (a+b+1)/2;
                if(panelInner.getComponent(s).getLocation().y>=Y)
                        b = s-1;
                else
                        a = s;
                System.out.println(a+" "+b);
        }
        
        try{
                while(a>=0&&panelInner.getComponent(a).getClass()!=JInfinityScrollViewItem.class)
                        a--;
                if(a<0)a=0;
                
                System.out.println("BinarySearch result: "+a+" -> "+panelInner.getComponent(a).getClass());
                //panelInner.getComponent(a).setBackground(Color.BLACK);
                scrollPane.getVerticalScrollBar().setValue(panelInner.getComponent(a).getLocation().y);//scroll to next image
        }catch(Exception e){
                e.printStackTrace();
        }
	}	

	@Override
	public void scrollTo(int k) {
		this.scrollPane.getVerticalScrollBar().setValue(this.panelInner.getComponent(2*k).getLocation().y);
	}

	@Override
	public synchronized void notifyUpdate() {
		if(model.tryGet(showedItems)){
            final Meme meme = model.get(showedItems);

            if(meme == null){     // FIXME: to jest prawdopodobnie nieosiągalna ścieżka wykonania
                //TODO dodać obłsugę gdy nastąpi koniec danych
                LOG.debug("No more data here");

            }else{
                showedItems++;
                executorService.execute(new Runnable() {
public void run() {
                        LOG.debug("Asynchronous task");

// TODO Dodać ładowanie więcej niż jednego obrazka
final JInfinityScrollViewItem imagePanel = new JInfinityScrollViewItem(meme, metadata);

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
	public synchronized void refresh() {
		showedItems = 0;
		
		panelInner.removeAll();
		
		if(model.tryGet(showedItems)){
			notifyUpdate();
		}
	}

	@Override
	public synchronized void notifyStreamEnd(){
		System.out.println("NotifyStreamEnd");
		lblSpinner.setVisible(false);
		lblEnd.setVisible(true);
	}
	
	private void thereIsNoModel(){
		if(model!=null)
			lblSpinner.setVisible(true);
		else lblSpinner.setVisible(false);
	}
}
