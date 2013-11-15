package pl.edu.uj.tcs.memoizer.plugins;

import java.util.List;

public interface IPluginLoader {

	void addPluginDirectory(String dir);

	void clearPluginDirectories();

	void loadPlugins();

	List<IPluginFactory> getLoadedPluginFactories();
}
