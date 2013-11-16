package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.uj.tcs.memoizer.events.EventService;
import pl.edu.uj.tcs.memoizer.events.IEventObserver;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.DownloadPluginMock;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.MemeDownloadedObsMock;

public class ScheduledMemeDownloaderTest {

	private IEventService es = new EventService();
	private MemeDownloadedObsMock eom = new MemeDownloadedObsMock();
	
	@Before 
	public void prepareTests() throws NoSuchFieldException, SecurityException {
		es.attach(eom);
	}
	
	@Test
	public void isRunningTest() {
		IScheduledMemeDownloader isd = new ScheduledMemeDownloader(
				new DownloadPluginMock("plugin"), es);
		Assert.assertFalse(isd.isRunning());
		isd.start();
		Assert.assertTrue(isd.isRunning());
		isd.stop();
		Assert.assertFalse(isd.isRunning());
	}
	
	@Test
	public void downloadMemeTest() throws InterruptedException {
		IScheduledMemeDownloader isd = new ScheduledMemeDownloader(
				new DownloadPluginMock("plugin"), es);
		int old = eom.getCounter();
		isd.setMinRefreshRate(5);
		isd.start();
		Thread.sleep(30);
		isd.stop();
		Assert.assertTrue(eom.getCounter() > old);
	}
}
