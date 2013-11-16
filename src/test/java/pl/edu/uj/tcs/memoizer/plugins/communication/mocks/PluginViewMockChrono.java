package pl.edu.uj.tcs.memoizer.plugins.communication.mocks;

import java.util.List;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class PluginViewMockChrono implements IPluginView {

	@Override
	public EViewType getViewType() {
		return EViewType.CHRONOLOGICAL;
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
