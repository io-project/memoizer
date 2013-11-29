package pl.edu.uj.tcs.memoizer.serialization;

import net.sf.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import pl.edu.uj.tcs.memoizer.events.EventService;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.events.exceptions.EventException;

public class StateMapTest {

	private static class SimpleSink implements IStateSink {
		
		public String object;

		@Override
		public void saveData(String object) {
			this.object = object;
		}
	}
	
	private static class SimpleSource implements IStateSource {

		private String source;

		public SimpleSource(String source) {
			this.source = source;
		}

		@Override
		public String getData() {
			return source;
		}
	}
	
	@Test
	public void simpleConstructorTest() {
		StateMap sm = new StateMap(new SimpleSink());
	}
	
	private StateMap getPreparedMap(IStateSink sink, String[] names) throws DeserializationException {
		
		JSONObject joMather = new JSONObject();

		for(String name: names) {
			JSONObject jo = new JSONObject();
			jo.put("name", name);
			joMather.put(name, jo);
		}

		SimpleSource ss = new SimpleSource(joMather.toString());
		
		StateMap sm = new StateMap(ss, sink);
		return sm;
	}
	
	@Test
	public void sourceConstructorTest() throws DeserializationException {
		
		StateMap sm = getPreparedMap(new SimpleSink(), new String[]{"jo1", "jo2", "jo3"});
		IStateObject so;
		so = sm.get("jo1");
		Assert.assertNotNull(so);
		Assert.assertEquals(so.getJSON().get("name"), "jo1");
		so = sm.get("jo2");
		Assert.assertNotNull(so);
		Assert.assertEquals(so.getJSON().get("name"), "jo2");
		so = sm.get("jo3");
		Assert.assertNotNull(so);
		Assert.assertEquals(so.getJSON().get("name"), "jo3");
	}
	
	@Test
	public void saveTest() throws EventException, InterruptedException, DeserializationException {
		IEventService es = new EventService();
		SimpleSink ss = new SimpleSink();
		StateMap sm = getPreparedMap(ss, new String[] {"jo1", "jo2", "jo3"});
		es.attach(sm);
		IStateObject so = sm.get("jo3");
		so.getJSON().put("ola", "maKota");
		es.call(new SaveStateEvent());
		Thread.sleep(50);
		JSONObject jo = JSONObject.fromObject(ss.object);
		Assert.assertEquals("maKota", ((JSONObject) jo.get("jo3")).get("ola"));
	}
}
