package pl.edu.uj.tcs.memoizer.plugins.communication.mocks;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.serialization.StateObject;

public class DownloadPluginMock implements IDownloadPlugin {
	
	private String name;
	private EViewType curViewType = EViewType.CHRONOLOGICAL;
	
	public DownloadPluginMock(String name) {
		this.name = name;
	}

	@Override
	public StateObject getState() {
		return new StateObject();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<EViewType> getAvailableViews() {
		return Arrays.asList(EViewType.values());
	}

	@Override
	public void setView(EViewType viewType) {
		this.curViewType = viewType;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Meme getNext() {
		try {
			return new Meme(new URL("http://www.github.com"), 
					new URL("http://www.github.com"), 
					"title", 
					"description", 
					0, 
					0, 
					null, 
					curViewType,
					this);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Iterable<Meme> getNext(int n) {
		return new ArrayList<Meme>();
	}

}
