package pl.edu.uj.tcs.memoizer.plugins.communication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.Assert;
import org.junit.Test;

import pl.edu.uj.tcs.memoizer.plugins.EViewType;
import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.IPluginView;
import pl.edu.uj.tcs.memoizer.plugins.InvalidPluginException;
import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.DownloadPluginMock;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.EmptyDownloadPluginMock;
import pl.edu.uj.tcs.memoizer.plugins.communication.mocks.PluginViewMock;

public class MemeProviderTest {

	@Test
	public void constructorTest() {
		MemeProvider mp = new MemeProvider();
		Assert.assertNull(mp.getCurrentView());
	}
	
	@Test
	public void setViewTest() throws InvalidPluginException {
		MemeProvider mp = new MemeProvider();
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new DownloadPluginMock("plug1", EViewType.CHRONOLOGICAL, null));
		plugs.add(new DownloadPluginMock("plug2", EViewType.CHRONOLOGICAL, null));
		IPluginView pvc = new PluginViewMock(EViewType.CHRONOLOGICAL);
		mp.setView(pvc, plugs);
		Assert.assertEquals(pvc, mp.getCurrentView());
	}

	@Test(expected=InvalidPluginException.class)
	public void setViewTestFail() throws InvalidPluginException {
		MemeProvider mp = new MemeProvider();
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new DownloadPluginMock("plug1", EViewType.UNSEEN, null));
		plugs.add(new DownloadPluginMock("plug2", EViewType.FAVOURITE, null));
		IPluginView pvc = new PluginViewMock(EViewType.CHRONOLOGICAL);
		mp.setView(pvc, plugs);
		Assert.assertEquals(pvc, mp.getCurrentView());
	}
	
	@Test
	public void hasNextTestTrue() throws InvalidPluginException, InterruptedException {
		MemeProvider mp = new MemeProvider();
		IPluginView plv = new PluginViewMock(EViewType.CHRONOLOGICAL);
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new DownloadPluginMock("plug1", EViewType.CHRONOLOGICAL, null));
		plugs.add(new DownloadPluginMock("plug2", EViewType.CHRONOLOGICAL, null));
		plugs.add(new DownloadPluginMock("plug3", EViewType.CHRONOLOGICAL, null));
		mp.setView(plv, plugs);
		Thread.sleep(100);
		Assert.assertTrue(mp.hasNext());
	}

	@Test
	public void hasNextTestFalse() throws InvalidPluginException, InterruptedException {
		MemeProvider mp = new MemeProvider();
		Assert.assertFalse(mp.hasNext());
	}
	
	@Test
	public void getNextTest() throws ExecutionException, DownloadMemeException {
		MemeProvider mp = new MemeProvider();
		IPluginView plv = new PluginViewMock(EViewType.UNSEEN);
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new DownloadPluginMock("plug1", EViewType.UNSEEN, null));
		plugs.add(new DownloadPluginMock("plug2", EViewType.UNSEEN, null));
		mp.setView(plv, plugs);
		for(int i = 0; i < 5; i++) {
			Assert.assertNotNull(mp.getNext());
		}
	}

	@Test(expected=DownloadMemeException.class)
	public void getNextTestFail() throws ExecutionException, DownloadMemeException {
		MemeProvider mp = new MemeProvider();
		IPluginView plv = new PluginViewMock(EViewType.UNSEEN);
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new EmptyDownloadPluginMock("plug1", EViewType.UNSEEN, null));
		plugs.add(new EmptyDownloadPluginMock("plug2", EViewType.UNSEEN, null));
		mp.setView(plv, plugs);
		mp.getNext();
	}

	@Test
	public void getNextNTest() throws ExecutionException, DownloadMemeException {
		MemeProvider mp = new MemeProvider();
		IPluginView plv = new PluginViewMock(EViewType.FAVOURITE);
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new DownloadPluginMock("plug1", EViewType.FAVOURITE, null));
		plugs.add(new DownloadPluginMock("plug2", EViewType.FAVOURITE, null));
		plugs.add(new DownloadPluginMock("plug3", EViewType.FAVOURITE, null));
		mp.setView(plv, plugs);
		List<Meme> results = mp.getNext(10);
		Assert.assertFalse(results.isEmpty());
		for(Meme m: results) {
			Assert.assertNotNull(m);
		}
	}

	@Test(expected=DownloadMemeException.class)
	public void getNextNTestFail() throws ExecutionException, DownloadMemeException {
		MemeProvider mp = new MemeProvider();
		IPluginView plv = new PluginViewMock(EViewType.FAVOURITE);
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new EmptyDownloadPluginMock("plug1", EViewType.FAVOURITE, null));
		plugs.add(new EmptyDownloadPluginMock("plug2", EViewType.FAVOURITE, null));
		plugs.add(new EmptyDownloadPluginMock("plug3", EViewType.FAVOURITE, null));
		mp.setView(plv, plugs);
		List<Meme> results = mp.getNext(10);
	}

	@Test
	public void stopTest() throws InvalidPluginException {
		MemeProvider mp = new MemeProvider();
		ArrayList<IDownloadPlugin> plugs = new ArrayList<>();
		plugs.add(new DownloadPluginMock("plug1", EViewType.CHRONOLOGICAL, null));
		plugs.add(new DownloadPluginMock("plug2", EViewType.CHRONOLOGICAL, null));
		plugs.add(new DownloadPluginMock("plug3", EViewType.CHRONOLOGICAL, null));
		mp.setView(new PluginViewMock(EViewType.CHRONOLOGICAL), plugs);
		mp.stop();
	}
}
