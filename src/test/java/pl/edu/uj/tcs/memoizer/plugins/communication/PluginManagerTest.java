package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.uj.tcs.memoizer.events.EventService;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPlugin;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.DownloadPluginMock;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.PluginFactoryMockFactory;

public class PluginManagerTest {
	
	private PluginFactoryMockFactory inceptionMock = new PluginFactoryMockFactory();
	private IEventService es = new EventService();
	private PluginManager pm;
	private ArrayList<IPluginFactory> firstFacts;
	
	@Before
	public void initManger() {
		firstFacts = new ArrayList<>();
		for(int i = 0; i < 10; i++) {
			firstFacts.add(inceptionMock.getAnotherFactory());
		}
	}
	
	@Test
	public void constructorTest() {
		pm = new PluginManager(firstFacts, es);
	}
	
	@Test
	public void setFactoriesTest() {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>(firstFacts);
		newFacts.remove(newFacts.size()-1);
		for(int i = 0; i < 5; i++) {
			newFacts.add(inceptionMock.getAnotherFactory());
		}
		pm = new PluginManager(firstFacts, es);
		pm.setPluginFactories(newFacts);
	}
	
	@Test
	public void getLoadedPluginsTest() {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		for(int i = 0; i < 10; i++) {
			newFacts.add(inceptionMock.getAnotherFactory());
		}
		pm = new PluginManager(newFacts, es);
		List<IPlugin> plugs = pm.getLoadedPlugins();
		Assert.assertEquals(10, plugs.size());

		HashSet<String> fset = new HashSet<String>();
		HashSet<String> pset = new HashSet<String>();
		for(IPluginFactory ipf: newFacts) {
			fset.add(ipf.getPluginName());
		}
		for(IPlugin pl: plugs) {
			pset.add(pl.getName());
		}
		Assert.assertEquals(pset.size(), fset.size());
		for(IPlugin pl: plugs) {
			Assert.assertTrue(fset.contains(pl.getName()));
		}
		
		newFacts = new ArrayList<IPluginFactory>();
		newFacts.add(inceptionMock.getAnotherFactory());
		pm.setPluginFactories(newFacts);
		plugs = pm.getLoadedPlugins();
		Assert.assertEquals(plugs.size(), newFacts.size());
		Assert.assertEquals(plugs.get(0).getName(), newFacts.get(0).getPluginName());
	}

	@Test(expected=InvalidPlugin.class)
	public void failsSaveStateOfTest() throws InvalidPlugin {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		for(int i = 0; i < 10; i++) {
			newFacts.add(inceptionMock.getAnotherFactory());
		}
		pm = new PluginManager(newFacts, es);
		pm.saveStateOf(new DownloadPluginMock("blablabalafail"));
	}
	
	@Test(expected=InvalidPlugin.class)
	public void failsReloadStateOfTest() throws InvalidPlugin {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		for(int i = 0; i < 10; i++) {
			newFacts.add(inceptionMock.getAnotherFactory());
		}
		pm = new PluginManager(newFacts, es);
		pm.reloadStateOf(new DownloadPluginMock("blablabalafail"));
	}
	
	public void getAllNamesTest() {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		for(int i = 0; i < 30; i++) {
			newFacts.add(inceptionMock.getAnotherFactory());
		}
		pm = new PluginManager(newFacts, es);

		HashSet<String> f = new HashSet<>(pm.getAllPluginNames());
		for(IPluginFactory fact: newFacts) {
			Assert.assertTrue(f.contains(fact.getPluginName()));
		}
	}
	
	public void getPluginForNameTest() {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		for(int i = 0; i < 10; i++) {
			newFacts.add(inceptionMock.getAnotherFactory());
		}
		pm = new PluginManager(newFacts, es);
		for(IPluginFactory fact: newFacts) {
			Assert.assertEquals(fact.getPluginName(), 
					pm.getPluginForName(fact.getPluginName()).getName());
		}
		
		IPluginFactory fact = inceptionMock.getAnotherFactory();
		Assert.assertNull(pm.getPluginForName(fact.getPluginName()));
	}
	
	@Test
	public void getAvailableViewsTest() {
		// it is supposed to return every type since plugin mock provides every one
		pm = new PluginManager(firstFacts, es);
		List<EViewType> vtypes = pm.getAvailableViews();
		HashSet<EViewType> hs = new HashSet<>(vtypes);
		Assert.assertEquals(hs.size(), EViewType.values().length);
	}
	
	@Test
	public void getAvailableViewsEmptyTest() {
		pm = new PluginManager(es);
		
		Assert.assertTrue(pm.getAvailableViews().isEmpty());
	}
}
