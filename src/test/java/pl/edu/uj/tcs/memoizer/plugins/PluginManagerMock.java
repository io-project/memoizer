package pl.edu.uj.tcs.memoizer.plugins;

import java.util.ArrayList;
import java.util.List;

public class PluginManagerMock implements IPluginManager {
	
	private IDownloadPlugin pluginMock = new DownloadPluginMock();
	private ArrayList<IPluginFactory> plugins = new ArrayList<>();
	
	{
		//plugins.add(new DownloadPluginMock());
	}

	@Override
	public void addPluginDirectory(String dir) { }

	@Override
	public void clearPluginDirectories() { }

	@Override
	public void loadPlugins() { }

	@Override
	public List<IPluginFactory> getLoadedPluginFactories() {
		return plugins;
	}

}
