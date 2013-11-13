package pl.edu.uj.tcs.memoizer.plugins;

import java.util.List;

public interface IPluginManager {

	void addPluginDirectory(String dir);

	void clearPluginDirectories();

	void loadPlugins();

	List<IPluginFactory> getLoadedPluginFactories();
}
