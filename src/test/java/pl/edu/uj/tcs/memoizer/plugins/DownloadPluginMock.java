package pl.edu.uj.tcs.memoizer.plugins;

import java.awt.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.naming.directory.InvalidAttributesException;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class DownloadPluginMock implements IDownloadPlugin {
	
	EViewType currentView = EViewType.CHRONOLOGICAL;

	@Override
	public Map<String, Object> getState() {
		return null;
	}

	@Override
	public List<EViewType> getAvailableViews() {
		return Arrays.asList(new EViewType[]{EViewType.CHRONOLOGICAL, EViewType.UNSEEN });
	}

	@Override
	public void setView(EViewType viewType) { 
		if(viewType == EViewType.CHRONOLOGICAL || viewType == EViewType.UNSEEN) {
			this.currentView = viewType;
		} 
	}
	
	@Override
	public boolean hasNext(){
		return true;
	}
	
	@Override
	public Meme getNext(){
		return null;
	}
	
	@Override
	public Iterable<Meme> getNext(int n){
		return null;
	}

}
