package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.junit.Assert;

import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPlugin;
import pl.edu.uj.tcs.memoizer.serialization.StateObject;


/**
 * 
 * @author ziemin
 * 
 * IPluginManager implementation
 * This class is meant to by used only by one thread
 *
 */
public class PluginManager implements IPluginManager {
	
	private TreeMap<String, IPlugin> plugMap = new TreeMap<>();
	private HashMap<String, IPluginFactory> factMap = new HashMap<>();
	final public IEventService eventService;
	
	private Logger LOG = Logger.getLogger(PluginManager.class);
	
	public PluginManager(IEventService eventService) {
		this.eventService = eventService;
	}

	public PluginManager(List<IPluginFactory> factories, IEventService eventService) {
		this(eventService);
		setPluginFactories(factories);
	}

	@Override
	public void setPluginFactories(List<IPluginFactory> factories) {
		
		factMap.clear();
		TreeMap<String, IPlugin> newPlugs = new TreeMap<>();

		for(IPluginFactory fact: factories) {

			// all names must be unique
			Assert.assertFalse(factMap.containsKey(fact.getPluginName())); 
			factMap.put(fact.getPluginName(), fact);

			StateObject state;
			IPlugin plugin = plugMap.get(fact.getPluginName());
			if(plugin != null) {
				state = plugin.getState();
			} else {
				state = getLatestStateOf(plugin);
			}
			
			plugin = fact.newInstance(state);
			Assert.assertEquals("Plugin name and factory name should be same",
					plugin.getName(), fact.getPluginName());
			newPlugs.put(plugin.getName(), plugin);
		}
		
		// saving states of old plugins which are not included
		for(Entry<String, IPlugin> entry: plugMap.entrySet()) {

			if(!newPlugs.containsKey(entry.getKey())) {
				try {
					saveStateOf(entry.getValue());
				} catch(InvalidPlugin e) {
					// should never happen
					LOG.error("Could not save state of plugin currently loaded: " + e.getMessage());
				}
			}
		}
		
		plugMap = newPlugs;
	}
	
	private StateObject getLatestStateOf(IPlugin plugin) {
		// TODO get state from appropriate manager
		return null;
	}
	
	@Override
	public void saveStateOf(IPlugin plugin) throws InvalidPlugin {
		
		if(!plugMap.containsKey(plugin.getName())) {
			throw new InvalidPlugin(plugin.getName() + " not loaded");
		}

		// TODO save the state 
	}
	
	@Override
	public void saveStates() {
		for(IPlugin plug: plugMap.values()) {
			try {
				saveStateOf(plug);
			} catch(InvalidPlugin e) {
				// should never happen
				LOG.error("Could not save state of plugin currently loaded: " + e.getMessage());
			}
		}
	}
	
	@Override
	public void reloadStateOf(IPlugin plugin) throws InvalidPlugin {
		
		if(!plugMap.containsKey(plugin.getName())) {
			throw new InvalidPlugin(plugin.getName() + " not loaded");
		}
		
		Assert.assertTrue("No factory for plugin: " + plugin.getName() + " in pluginManager: ", 
				factMap.containsKey(plugin.getName()));

		StateObject state = getLatestStateOf(plugin);

		IPluginFactory fact = factMap.get(plugin.getName());
		plugin = fact.newInstance(state);
		Assert.assertEquals("Plugin name and factory name should be same", 
				plugin.getName(), fact.getPluginName());

		plugMap.put(plugin.getName(), plugin);
	}
	
	@Override
	public void reloadStates() {
		for(IPlugin plug: plugMap.values()) {
			try {
				reloadStateOf(plug);
			} catch(InvalidPlugin e) {
				// should never happen
				LOG.error("Could not save state of plugin currently loaded: " + e.getMessage());
			}
		}
	}

	@Override
	public List<EViewType> getAvailableViews() {

		List<EViewType> result = new ArrayList<EViewType>();

		for(IPlugin plugin: plugMap.values()) {

			if(plugin instanceof IDownloadPlugin) {
				result.addAll(((IDownloadPlugin) plugin).getAvailableViews());
			}
		}

		return result;
	}

	@Override
	public List<IPlugin> getLoadedPlugins() {
		return new ArrayList<IPlugin>(plugMap.values());
	}

	@Override
	public List<IDownloadPlugin> getPluginsForView(EViewType viewType) {

		List<IDownloadPlugin> result = new ArrayList<IDownloadPlugin>();
		for(IPlugin plugin: plugMap.values()) {

			if(plugin instanceof IDownloadPlugin) {
				if(((IDownloadPlugin) plugin).getAvailableViews().contains(viewType)) {
					result.add((IDownloadPlugin) plugin);
				}
			}
		}

		return result;
	}

	@Override
	public List<String> getAllPluginNames() {

		return new ArrayList<String>(factMap.keySet());
	}

	@Override
	public IPlugin getPluginForName(String pluginName) {

		if(!plugMap.containsKey(pluginName)) {
			return null;
		}
		return plugMap.get(pluginName);
	}
	
}
