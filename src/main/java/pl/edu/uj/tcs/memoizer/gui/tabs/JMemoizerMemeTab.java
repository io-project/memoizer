package pl.edu.uj.tcs.memoizer.gui.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
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
import pl.edu.uj.tcs.memoizer.plugins.InvalidPlugin;
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
				//TODO brać jakieś sensowne IPluginView
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
		} catch (InvalidPlugin e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		IMemoizerModel model = new SimpleMemoizerModel(memeProvider);
		
		JMemoizerView view = new JInfinityScrollView();
		model.bindView(view);
		view.attachModel(model);
		
		this.panel = view;
	}
	
	public JMemoizerMemeTab(EViewType viewType, IDownloadPlugin plugin){
		this(plugin.getName(), viewType, Arrays.asList(new IDownloadPlugin[]{plugin}));
	}
	
	@Override
	public JPanel getViewport() {
		return this.panel;
	}

}
