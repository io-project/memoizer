package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import pl.edu.uj.tcs.memoizer.events.EventService;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPlugin;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.DownloadPluginMock;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.GuiHandlerMock;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.PluginViewMockChrono;

public class MemeProviderTest {

	private IEventService es = new EventService();
	private GuiHandlerMock gm = new GuiHandlerMock();
	private PluginViewMockChrono pvc = new PluginViewMockChrono();
	
	@Test
	public void constructorTest() {
		MemeProvider mp = new MemeProvider(es, gm);
		Assert.assertNull(mp.getCurrentView());
	}
	
	@Test
	public void setViewTest() throws InvalidPlugin {
		MemeProvider mp = new MemeProvider(es, gm);
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new DownloadPluginMock("plug1"));
		plugs.add(new DownloadPluginMock("plug2"));
		mp.setView(pvc, plugs);
		Assert.assertEquals(pvc, mp.getCurrentView());
	}
	
	@Test
	public void handlingTest() throws InvalidPlugin, InterruptedException {
		MemeProvider mp = new MemeProvider(es, gm);
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new DownloadPluginMock("plug1"));
		plugs.add(new DownloadPluginMock("plug2"));
		plugs.add(new DownloadPluginMock("plug3"));
		mp.setView(pvc, plugs);
		int old = gm.getHandledCount();
		Thread.sleep(100);
		Assert.assertTrue(old < gm.getHandledCount());
	}
	
	@Test
	public void cancelTest() throws InvalidPlugin {
		MemeProvider mp = new MemeProvider(es, gm);
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new DownloadPluginMock("plug1"));
		plugs.add(new DownloadPluginMock("plug2"));
		plugs.add(new DownloadPluginMock("plug3"));
		mp.setView(pvc, plugs);
		mp.cancel();
	}
}
