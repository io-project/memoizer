package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.List;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException;
import pl.edu.uj.tcs.memoizer.plugins.InvalidViewException;

public interface IPluginManager {

	/**
	 *  Sets new factories and a set of loaded plugins at the same time
	 *  Upon execution of this method getLoadedPlugins should return 
	 *  a plugin for every factory
	 */
	void setPluginFactories(List<IPluginFactory> factories);
	
	/**
	 * @return a list of names of every plugin provided by factories given to manager
	 */
	List<String> getAllPluginNames();
	
	/**
	 * @param name of desired plugin
	 * @return new IDownloadPlugin instance for given name and viewType
	 * @throws InvalidPluginException if name does not apply to any plugin 
	 * @throws InvalidViewException if plugin of name pluginName does not provide viewType
	 */
	IDownloadPlugin getPluginFor(String pluginName, EViewType viewType) throws InvalidPluginException, InvalidViewException;

	/**
	 * @return every view type that is provided by at least one plugin
	 */
	List<EViewType> getAvailableViews();
	
	/**
	 * @param viewType
	 * @return a list of new instances of plugins providing given view
	 */
	List<IDownloadPlugin> getPluginsForView(EViewType viewType);
	
}
