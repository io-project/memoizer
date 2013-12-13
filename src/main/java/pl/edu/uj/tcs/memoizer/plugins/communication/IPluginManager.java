package pl.edu.uj.tcs.memoizer.plugins.communication;

import pl.edu.uj.tcs.memoizer.plugins.*;

import java.util.List;

/**
 * @author Marcin Ziemiński
 * @author Paweł Kubiak
 * @author Maciej Poleski
 */
public interface IPluginManager extends IPluginManagerClient {


    /**
     * Sets new factories and a set of loaded plugins at the same time
     * Upon execution of this method getLoadedPlugins should return
     * a plugin for every factory
     *
     * @param factories
     */
    void setPluginFactories(List<IPluginFactory> factories);

    /**
     * @param pluginName name of desired plugin
     * @param viewType   view type
     * @param parameters additional parameter for view
     * @return new IDownloadPlugin instance for given name and viewType
     * @throws pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException
     *          if name does not apply to any plugin
     * @throws pl.edu.uj.tcs.memoizer.plugins.InvalidViewException
     *          if plugin of name pluginName does not provide viewType
     */
    IDownloadPlugin getPluginInstanceForView(String pluginName, EViewType viewType, Object parameters) throws InvalidPluginException, InvalidViewException;

    /**
     * Simplified version of getPluginInstanceForView for views which doesn't require additional parameters.
     *
     * @param pluginName name of desired plugin
     * @param viewType   view type
     * @return new IDownloadPlugin instance for given name and viewType
     * @throws pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException
     *          if name does not apply to any plugin
     * @throws pl.edu.uj.tcs.memoizer.plugins.InvalidViewException
     *          if plugin of name pluginName does not provide viewType
     */
    IDownloadPlugin getPluginInstanceForView(String pluginName, EViewType viewType) throws InvalidPluginException, InvalidViewException;

    /**
     * Simplified version getPluginsInstancesForView for views which doesn't require additional parameters.
     *
     * @param viewType view type
     * @return a list of new instances of plugins providing given view
     */
    List<IDownloadPlugin> getPluginsInstancesForView(EViewType viewType);

    /**
     * @param viewType   view type
     * @param parameters additional parameter for view
     * @return a list of new instances of plugins providing given view
     */
    List<IDownloadPlugin> getPluginsInstancesForView(EViewType viewType, Object parameters);
}
