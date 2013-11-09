package pl.edu.uj.tcs.memoizer.plugins;

import java.awt.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.naming.directory.InvalidAttributesException;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.MemeInfo;

public class DownloadPluginMock implements IDownloadPlugin {
	
	EViewType currentView = EViewType.CHRONOLOGICAL;

	@Override
	public String getName() {
		return "DownloadPluginMock";
	}

	@Override
	public Image getIcon() {
		return null;
	}

	@Override
	public Map<String, Object> getProperties() {
		return null;
	}

	@Override
	public void setProperties(Map<String, Object> properties) { }

	@Override
	public Iterable<MemeInfo> getRecordsSinceLast() {
		return null;
	}

	@Override
	public Iterable<MemeInfo> getTopRecords() {
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

}
