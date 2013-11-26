package pl.edu.uj.tcs.memoizer.plugins.communication.mocks;

import java.util.List;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class PluginViewMock implements IPluginView {

	private EViewType viewType;
	
	public PluginViewMock(EViewType viewType) {
		this.viewType = viewType;
	}

	@Override
	public EViewType getViewType() {
		return viewType;
	}

	@Override
	public Meme extractNextMeme(List<Meme> memes) {
		if(!memes.isEmpty()) {
			return memes.remove(0);
		} else {
			return null;
		}
	}
	
}
