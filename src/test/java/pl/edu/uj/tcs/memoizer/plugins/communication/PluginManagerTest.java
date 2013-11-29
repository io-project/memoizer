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
import pl.edu.uj.tcs.memoizer.plugins.IPluginFactory;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException;
import pl.edu.uj.tcs.memoizer.plugins.InvalidViewException;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.PluginFactoryMockFactory;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.SingleViewPluginFactoryMock;

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
	
	public void getAllNamesTest() {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		for(int i = 0; i < 30; i++) {
			newFacts.add(inceptionMock.getAnotherFactory());
		}
		pm = new PluginManager(newFacts, es);

		HashSet<String> f = new HashSet<>(pm.getAllPluginNames());
		for(IPluginFactory fact: newFacts) {
			Assert.assertTrue(f.contains(fact.getServiceName()));
		}
	}

	public void getPluginForView() throws InvalidPluginException {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		for(int i = 0; i < 10; i++) {
			newFacts.add(inceptionMock.getAnotherFactory());
		}
		pm = new PluginManager(newFacts, es);
		for(IPluginFactory fact: newFacts) {
			Assert.assertEquals(fact.getServiceName(), 
					pm.getPluginInstanceForView(fact.getServiceName(), EViewType.CHRONOLOGICAL).getServiceName());
		}
	}
	
	public void getPluginForTest() throws InvalidPluginException {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		for(int i = 0; i < 10; i++) {
			newFacts.add(inceptionMock.getAnotherFactory());
		}
		pm = new PluginManager(newFacts, es);
		for(IPluginFactory fact: newFacts) {
			Assert.assertEquals(fact.getServiceName(), 
					pm.getPluginInstanceForView(fact.getServiceName(), EViewType.CHRONOLOGICAL).getServiceName());
		}
	}
	
	@Test
	public void getPluginsForViewTest() {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		IPluginFactory chF = new SingleViewPluginFactoryMock("chF", EViewType.CHRONOLOGICAL);
		IPluginFactory favF = new SingleViewPluginFactoryMock("favF", EViewType.FAVOURITE);
		IPluginFactory unF = new SingleViewPluginFactoryMock("unF", EViewType.UNSEEN);
		IPluginFactory qf = new SingleViewPluginFactoryMock("qF", EViewType.QUEUE);
		newFacts.add(chF);
		newFacts.add(favF);
		newFacts.add(unF);
		newFacts.add(qf);
		pm = new PluginManager(es);
		pm.setPluginFactories(newFacts);
		Assert.assertEquals(chF.getServiceName(), pm.getPluginsInstancesForView(EViewType.CHRONOLOGICAL).get(0).getServiceName());
		Assert.assertEquals(favF.getServiceName(), pm.getPluginsInstancesForView(EViewType.FAVOURITE).get(0).getServiceName());
		Assert.assertEquals(unF.getServiceName(), pm.getPluginsInstancesForView(EViewType.UNSEEN).get(0).getServiceName());
		Assert.assertEquals(qf.getServiceName(), pm.getPluginsInstancesForView(EViewType.QUEUE).get(0).getServiceName());
	}

	@Test(expected=InvalidPluginException.class)
	public void getPluginForTestNoName() throws InvalidPluginException {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		String name = "";
		for(int i = 0; i < 5; i++) {
			IPluginFactory fact = inceptionMock.getAnotherFactory();
			newFacts.add(fact);
			name += fact.getServiceName();
		}
		pm = new PluginManager(newFacts, es);
		pm.getPluginInstanceForView(name, EViewType.CHRONOLOGICAL);
	}

	@Test(expected=InvalidViewException.class)
	public void getPluginForTestNoView() throws InvalidPluginException {
		ArrayList<IPluginFactory> newFacts = new ArrayList<IPluginFactory>();
		IPluginFactory fact = new SingleViewPluginFactoryMock("plugin", EViewType.CHRONOLOGICAL);
		newFacts.add(fact);
		pm = new PluginManager(newFacts, es);
		pm.getPluginInstanceForView(fact.getServiceName(), EViewType.FAVOURITE);
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
