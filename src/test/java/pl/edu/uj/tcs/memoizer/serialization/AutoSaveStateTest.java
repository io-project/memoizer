package pl.edu.uj.tcs.memoizer.serialization;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sf.json.JSONObject;

import org.junit.Test;

import pl.edu.uj.tcs.memoizer.events.EventService;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.serialization.AutoSaveState;

public class AutoSaveStateTest {
	
	class TestSink implements IStateSink {
		
		public String serialized;

		@Override
		public void saveData(String object) {
			serialized = object;
		}
		
	}

	@Test
	public void serializationTest() throws InterruptedException, IOException {
		IEventService es = new EventService();
		TestSink sink = new TestSink();
		AutoSaveState as = new AutoSaveState(sink);
		as.changeSaveRate(50);
		JSONObject jo = as.getJSON();
		jo.put("Val", "val");
		Thread.sleep(150);
		String serialized = sink.serialized;
		AutoSaveState as2 = new AutoSaveState(serialized, sink);
		JSONObject jo2 = as2.getJSON();
		assertEquals(jo.get("Val"), jo2.get("Val"));
	}
}
