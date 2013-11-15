package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.List;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPlugin;

public interface IPluginManager {

	void setPluginFactories(List<IPluginFactory> factories);

	/**
	 * @return every view type that is provided by at least one plugin
	 */
	List<EViewType> getAvailableViews();
	
	/**
	 * @param viewType
	 * @return a list of plugins providing given view
	 */
	List<IDownloadPlugin> getPluginsForView(EViewType viewType);
	
	/**
	 * Saves states of all plugins
	 */
	void saveStates();
	/**
	 * Saves states of particular plugin
	 * @throws InvalidPlugin if there is no such plugin loaded in manager
	 */
	void saveStateOf(IPlugin plugin) throws InvalidPlugin;

	
	/**
	 * Reloads states of all plugins
	 */
	void reloadStates();

	/**
	 * Reloads states of particular plugin
	 * @throws InvalidPlugin if there is no such plugin loaded in manager
	 */
	void reloadStateOf(IPlugin plugin) throws InvalidPlugin;

}
