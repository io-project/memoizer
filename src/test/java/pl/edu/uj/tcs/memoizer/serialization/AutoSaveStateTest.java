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
	
	@Test
	public void serializationTest() throws InterruptedException, IOException {
		OutputStream os = new ByteArrayOutputStream();
		IEventService es = new EventService();
		AutoSaveState as = new AutoSaveState(os);
		as.changeSaveRate(50);
		JSONObject jo = as.getJSON();
		jo.put("Val", "val");
		Thread.sleep(150);
		String serialized = os.toString();
		AutoSaveState as2 = new AutoSaveState(serialized, os);
		JSONObject jo2 = as2.getJSON();
		assertEquals(jo.get("Val"), jo2.get("Val"));
	}
}
