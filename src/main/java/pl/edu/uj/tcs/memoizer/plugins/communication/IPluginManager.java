package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.awt.Image;
import java.util.List;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
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
	 * @param pluginName
	 * @return icon from pluginFactory
	 */
	Image getIconForPluginName(String pluginName);
	
	/**
	 * @param viewType
	 * @return a list of names of every plugin supporting given viewType.
	 */
	List<String> getPluginsNamesForView(EViewType viewType);

	/**
	 * @return every view type that is provided by at least one plugin
	 */
	List<EViewType> getAvailableViews();

	/**
	 * @param name of desired plugin
	 * @return new IDownloadPlugin instance for given name and viewType
	 * @throws InvalidPluginException if name does not apply to any plugin 
	 * @throws InvalidViewException if plugin of name pluginName does not provide viewType
	 */
	List<IDownloadPlugin> getPluginsInstancesForView(List<String> pluginsName, EViewType viewType) throws InvalidPluginException, InvalidViewException;

	/**
	 * @param name of desired plugin
	 * @return new IDownloadPlugin instance for given name and viewType
	 * @throws InvalidPluginException if name does not apply to any plugin 
	 * @throws InvalidViewException if plugin of name pluginName does not provide viewType
	 */
	IDownloadPlugin getPluginInstanceForView(String pluginName, EViewType viewType) throws InvalidPluginException, InvalidViewException;


	
	/**
	 * @param viewType
	 * @return a list of new instances of plugins providing given view
	 */
	List<IDownloadPlugin> getPluginsInstancesForView(EViewType viewType);
	

	

	
}
