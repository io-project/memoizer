package pl.edu.uj.tcs.memoizer.gui.tabs;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.edu.uj.tcs.memoizer.gui.IconManager;
import pl.edu.uj.tcs.memoizer.gui.MetadataHandler;
import pl.edu.uj.tcs.memoizer.gui.models.ILegacyMemoizerModel;
import pl.edu.uj.tcs.memoizer.gui.models.SimpleMemoizerModel;
import pl.edu.uj.tcs.memoizer.gui.views.JInfinityScrollView;
import pl.edu.uj.tcs.memoizer.gui.views.JMemoizerView;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.plugins.communication.*;

public final class JMemoizerMemeTab extends JMemoizerTab {
	private JPanel panel;
	private ILegacyMemoizerModel model;
	
	public JMemoizerMemeTab(String title,final EViewType viewType, final List<String> pluginsNames, final IPluginManager pluginManager, final MetadataHandler metadataHandler){
		this.icon = IconManager.getIconForViewType(viewType);		
		this.title = title;
		
		
		final JMemoizerView view = new JInfinityScrollView(metadataHandler);
		
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		JPanel bar = new JPanel();
		bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
	
		//Generate meme view description
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;
		for(String plugin: pluginsNames){
			if(!isFirst)sb.append(", ");
			
			isFirst = false;
			sb.append(plugin);
		}

		
		JLabel description = new JLabel(viewType.getName()+": "+sb.toString());
		bar.add(description);
		
		bar.add(Box.createHorizontalGlue());
		
		JComboBox<String> comboBox = new JComboBox<>();
		comboBox.setModel(new DefaultComboBoxModel<>(new String[] {"Infinity Scroll", "Simple Infinity Scroll"}));
		comboBox.setPrototypeDisplayValue("XXXXXXXXXXX");
		
		//TODO fix hack bellow, to provent becoming combobox to huge
		int height = comboBox.getPreferredSize().height;
		System.out.println("PREF:"+comboBox.getPreferredSize());
		System.out.println("MAX:"+comboBox.getMaximumSize());

		comboBox.setMaximumSize(comboBox.getPreferredSize());
		
		bar.add(comboBox);
		
		bar.add(Box.createHorizontalStrut(4));

		//go-top button
		JButton buttonTopMeme = new JButton("↥");//⤒
        buttonTopMeme.setMargin(new Insets(0, 8, 0, 8));
        buttonTopMeme.setFont(new Font("Dialog", Font.PLAIN, 16));
        buttonTopMeme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.scrollTo(0);
			}
		});
        //TODO fix hack bellow
        Dimension x = new Dimension(buttonTopMeme.getPreferredSize().width, height);
        buttonTopMeme.setPreferredSize(x);
        buttonTopMeme.setMaximumSize(x);
        
		bar.add(buttonTopMeme);
		
		//previous-meme button
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
        //TODO fix hack bellow
        x = new Dimension(buttonPrevMeme.getPreferredSize().width, height);
        buttonPrevMeme.setPreferredSize(x);
        buttonPrevMeme.setMaximumSize(x);
        
		bar.add(buttonPrevMeme);
		
		//next-meme button
		JButton buttonNextMeme = new JButton("»");
		buttonNextMeme.setMargin(new Insets(0, 8, 0, 8));
        buttonNextMeme.setFont(new Font("Dialog", Font.BOLD, 20));
        buttonNextMeme.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				view.scrollToNext();
			}
		});
        //TODO fix hack bellow
        x = new Dimension(buttonNextMeme.getPreferredSize().width, height);
        buttonNextMeme.setPreferredSize(x);
        buttonNextMeme.setMaximumSize(x);
        
		bar.add(buttonNextMeme);

		p.add(bar);
		p.add(Box.createVerticalStrut(4));
		bar.invalidate();

		if(viewType==EViewType.SEARCH){
			bar = new JPanel();
			bar.setLayout(new BoxLayout(bar, BoxLayout.X_AXIS));
			final JTextField textField = new JTextField("");
			x = new Dimension(Integer.MAX_VALUE, height);
			textField.setMaximumSize(x);
			
			bar.add(textField);
			bar.add(Box.createHorizontalStrut(4));
			JButton buttonSearch = new JButton("Search");
			x = new Dimension(buttonSearch.getPreferredSize().width, height);
			buttonSearch.setMaximumSize(x);
			bar.add(buttonSearch);
			p.add(bar);
			
			buttonSearch.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
                    String searchKey = textField.getText();
                    //unbind previous model
                    if (model != null)
                        model.bindView(null);
                    model = getSearchLegacyMemoizerModel(searchKey, pluginManager, pluginsNames);
                    model.bindView(view);
                    view.attachModel(model);
					view.refresh();//TODO to fix
				}
			});
		}else{
			model = getLegacyMemoizerModel(viewType, pluginsNames, pluginManager, metadataHandler);
			
			model.bindView(view);
			view.attachModel(model);
		}
		
		
		p.add(view);
		this.panel = p;
	}

    /**
     * Tworzy model w sposób określony w pierwotnej implementacji.
     *
     * @param viewType        Typ widoku obsługiwany przez ten model.
     * @param pluginsNames    Nazwy plugin-ów z których będą czerpane dane (Mem-y).
     * @param pluginManager   Jakiś {@link IPluginManager}.
     * @param metadataHandler Jakiś {@link MetadataHandler}
     * @return Model obsługujący dany typ widoku i dany dobór plugin-ów (oraz wykorzystujący dany
     *         {@link MetadataHandler}).
     */
    static ILegacyMemoizerModel getLegacyMemoizerModel(final EViewType viewType, List<String> pluginsNames, IPluginManager pluginManager, MetadataHandler metadataHandler) {
        IMemeProvider memeProvider;
        if (viewType == EViewType.UNSEEN)
            memeProvider = new MemeProviderUnseen(metadataHandler);
        else memeProvider = new MemeProvider();

        List<IDownloadPlugin> plugins = pluginManager.getPluginsInstancesForView(pluginsNames, viewType);

        try {
            memeProvider.setView(new IPluginView() {
                @Override
                public EViewType getViewType() {
                    return viewType;
                }

                @Override
                public Meme extractNextMeme(List<Meme> memes) {
                    if (memes.size() > 0)
                        return memes.remove(0);
                    return null;
                }
            }, plugins);
        } catch (InvalidPluginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new SimpleMemoizerModel(memeProvider);
    }

    /**
     * Tworzy model w sposób określony w pierwotnej implementacji w celu obsługi wyszukiwania danego hasła.
     *
     * @param searchKey     Prawdopodobnie fraza do wyszukania przez implementacje pluginów.
     * @param pluginManager Jakiś {@link pl.edu.uj.tcs.memoizer.plugins.communication.IPluginManager}.
     * @param pluginsNames  Nazwy plugin-ów z których będą czerpane dane (Mem-y).
     * @return Model obsługujący dane wyszukiwanie.
     */
    static ILegacyMemoizerModel getSearchLegacyMemoizerModel(String searchKey, IPluginManager pluginManager, List<String> pluginsNames) {
        IMemeProvider memeProvider = new MemeProvider();
        List<IDownloadPlugin> plugins = pluginManager.getPluginsInstancesForView(pluginsNames, EViewType.SEARCH, searchKey);
        try {
            memeProvider.setView(new IPluginView() {
                @Override
                public EViewType getViewType() {
                    return EViewType.SEARCH;
                }

                @Override
                public Meme extractNextMeme(List<Meme> memes) {
                    if (memes.size() > 0)
                        return memes.remove(0);
                    return null;
                }
            }, plugins);
        } catch (InvalidPluginException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new SimpleMemoizerModel(memeProvider);
    }

    public JMemoizerMemeTab(EViewType viewType, String pluginName, IPluginManager pluginManager, MetadataHandler metadataHandler){
		this(pluginName, viewType, Arrays.asList(new String[]{pluginName}), pluginManager, metadataHandler);
	}
	
	@Override
	public JPanel getViewport() {
		return this.panel;
	}

}
