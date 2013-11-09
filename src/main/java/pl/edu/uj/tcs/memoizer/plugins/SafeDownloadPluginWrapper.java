package pl.edu.uj.tcs.memoizer.plugins;

import java.util.List;

public class SafeDownloadPluginWrapper extends SafePluginWrapper implements IDownloadPlugin {
	
	public SafeDownloadPluginWrapper(IDownloadPlugin plugin) {
		super(plugin);
	}

	@Override
	public Iterable<MemeInfo> getRecordsSinceLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<MemeInfo> getTopRecords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EViewType> getAvailableViews() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setView(EViewType viewType) {
		// TODO Auto-generated method stub

	}

}
