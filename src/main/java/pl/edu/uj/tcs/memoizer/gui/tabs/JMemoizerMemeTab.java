package pl.edu.uj.tcs.memoizer.gui.tabs;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections.ListUtils;

import pl.edu.uj.tcs.memoizer.gui.MainWindow;
import pl.edu.uj.tcs.memoizer.gui.models.IMemoizerModel;
import pl.edu.uj.tcs.memoizer.gui.models.SimpleMemoizerModel;
import pl.edu.uj.tcs.memoizer.gui.views.JInfinityScrollView;
import pl.edu.uj.tcs.memoizer.gui.views.JMemoizerView;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.plugins.communication.MemeProvider;

public final class JMemoizerMemeTab extends JMemoizerTab {
	private JPanel panel;
	
	public JMemoizerMemeTab(String title,final EViewType viewType, List<IDownloadPlugin> plugins){
		System.out.println("MemeTab: "+viewType+" = "+plugins);
		if(viewType==EViewType.CHRONOLOGICAL)
			this.icon = new ImageIcon(MainWindow.class.getResource("/icons/clock.gif"));
		else if(viewType==EViewType.FAVOURITE)
			this.icon = new ImageIcon(MainWindow.class.getResource("/icons/favourites.gif"));
		else
			this.icon = new ImageIcon(MainWindow.class.getResource("/icons/world.gif"));
		//TODO dodać inne ikonki lub przemieścić to do EViewType
		
		this.title = title;
		
		MemeProvider memeProvider = new MemeProvider();
		try {
			memeProvider.setView(new IPluginView() {
				@Override
				public EViewType getViewType() {
					return viewType;
				}
				@Override
				public Meme extractNextMeme(List<Meme> memes) {
					if(memes.size()>0)
						return memes.remove(0);
					return null;
				}
			}, plugins);
		} catch (InvalidPluginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final JMemoizerView view = new JInfinityScrollView();
		IMemoizerModel model = new SimpleMemoizerModel(memeProvider);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		JPanel bar = new JPanel();
		bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
	
		//Generate meme view description
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for(IPlugin plugin: plugins){
			if(!isFirst)sb.append(", ");
			
			isFirst = false;
			sb.append(plugin.getServiceName());
		}

		
		JLabel description = new JLabel(viewType.getName()+": "+sb.toString());
		bar.add(description);
		
		bar.add(Box.createHorizontalGlue());
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Infinity Scroll", "Simple Infinity Scroll"}));
		comboBox.setPrototypeDisplayValue("XXXXXXXXX");
		bar.add(comboBox);
		
		bar.add(Box.createHorizontalStrut(4));

		JButton buttonTopMeme = new JButton("↥");//⤒
        buttonTopMeme.setMargin(new Insets(0, 8, 0, 8));
        buttonTopMeme.setFont(new Font("Dialog", Font.PLAIN, 20));
        buttonTopMeme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.scrollTo(0);
			}
		});
		bar.add(buttonTopMeme);
		
		
		bar.add(Box.createHorizontalStrut(4));
		JButton buttonPrevMeme = new JButton("«");
        buttonPrevMeme.setMargin(new Insets(0, 8, 0, 8));
        buttonPrevMeme.setFont(new Font("Dialog", Font.BOLD, 20));
        buttonPrevMeme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.scrollToPrevious();
			}
		});
		bar.add(buttonPrevMeme);
		
		
		JButton buttonNextMeme = new JButton("»");
		buttonNextMeme.setMargin(new Insets(0, 8, 0, 8));
        buttonNextMeme.setFont(new Font("Dialog", Font.BOLD, 20));
        buttonNextMeme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.scrollToNext();
			}
		});
		bar.add(buttonNextMeme);

		p.add(bar);
		
		model.bindView(view);
		view.attachModel(model);
		p.add(view);
		this.panel = p;
	}
	
	public JMemoizerMemeTab(EViewType viewType, IDownloadPlugin plugin){
		this(plugin.getServiceName(), viewType, Arrays.asList(new IDownloadPlugin[]{plugin}));
	}
	
	@Override
	public JPanel getViewport() {
		return this.panel;
	}

}
