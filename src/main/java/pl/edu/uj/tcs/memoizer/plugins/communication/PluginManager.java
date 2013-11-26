package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.junit.Assert;

import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException;
import pl.edu.uj.tcs.memoizer.plugins.InvalidViewException;
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

		for(IPluginFactory fact: factories) {
			factMap.put(fact.getServiceName(), fact);
		}
	}
	

	private StateObject getLatestStateOf(String pluginName, EViewType viewType) {
		// TODO get state from appropriate manager
		return null;
	}
	
	@Override
	public List<EViewType> getAvailableViews() {

		Set<EViewType> result = new TreeSet<EViewType>();

		for(IPluginFactory f: factMap.values()) {
			result.addAll(f.getAvailableDownloadViews());
		}

		return new ArrayList<EViewType>(result);
	}

	@Override
	public List<IDownloadPlugin> getPluginsForView(EViewType viewType) {

		List<IDownloadPlugin> result = new ArrayList<IDownloadPlugin>();
		for(IPluginFactory f: factMap.values()) {

			if(f.getAvailableDownloadViews().contains(viewType)) {
				result.add(f.newInstance(
						getLatestStateOf(f.getServiceName(), viewType), viewType));
			}
		}

		return result;
	}

	@Override
	public List<String> getAllPluginNames() {

		return new ArrayList<String>(factMap.keySet());
	}

	@Override
	public IDownloadPlugin getPluginFor(String pluginName, EViewType viewType) throws InvalidPluginException, InvalidViewException {

		IPluginFactory f = factMap.get(pluginName);
		if(f == null) {
			throw new InvalidPluginException("There is no factory for plugin with name: " + pluginName);
		} else if(!f.getAvailableDownloadViews().contains(viewType)) {
			throw new InvalidViewException("Plugin : " + pluginName + 
					" does not provide given view type: " + viewType);
		}

		return f.newInstance(
				getLatestStateOf(f.getServiceName(), viewType), viewType);
	}
}
