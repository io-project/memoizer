package pl.edu.uj.tcs.memoizer.plugins.communication.mocks;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class EmptyDownloadPluginMock extends DownloadPluginMock {

	public EmptyDownloadPluginMock(String name, EViewType viewType) {
		super(name, viewType);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean hasNext() {
		return false;
	}

	@Override
	public Meme getNext() {
		return null;
	}

	@Override
	public Iterable<Meme> getNext(int n) {
		return null;
	}
	
}
