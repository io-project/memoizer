package pl.edu.uj.tcs.memoizer.plugins.communication;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException;
import pl.edu.uj.tcs.memoizer.plugins.InvalidViewException;

import java.awt.*;
import java.util.List;

/**
 * Ten interfejs zawiera wyciągniętą funkcjonalność menadżera plugin-ów na potrzeby klienta (GUI).
 *
 * @author Maciej Poleski
 */
public interface IPluginManagerClient {

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
     * @param viewType view type
     * @return a list of names of every plugin supporting given viewType.
     */
    List<String> getPluginsNamesForView(EViewType viewType);

    /**
     * @return every view type that is provided by at least one plugin
     */
    List<EViewType> getAvailableViews();

    /**
     * @param pluginsNames names of desired plugins
     * @param viewType     view type
     * @param parameters   additional parameter for view
     * @return new IDownloadPlugin instances for given names and viewType
     * @throws pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException
     *          if name does not apply to any plugin
     * @throws pl.edu.uj.tcs.memoizer.plugins.InvalidViewException
     *          if plugin of name pluginName does not provide viewType
     */
    List<IDownloadPlugin> getPluginsInstancesForView(List<String> pluginsName, EViewType viewType, Object parameters) throws InvalidPluginException, InvalidViewException;

    /**
     * Simplified version of getPluginsInstancesForView for views which doesn't require additional parameters.
     *
     * @param pluginsNames names of desired plugins
     * @param viewType     view type
     * @return new IDownloadPlugin instances for given names and viewType
     * @throws pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException
     *          if name does not apply to any plugin
     * @throws pl.edu.uj.tcs.memoizer.plugins.InvalidViewException
     *          if plugin of name pluginName does not provide viewType
     */
    List<IDownloadPlugin> getPluginsInstancesForView(List<String> pluginsName, EViewType viewType) throws InvalidPluginException, InvalidViewException;

}
