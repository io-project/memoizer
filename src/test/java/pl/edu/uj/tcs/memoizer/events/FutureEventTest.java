package pl.edu.uj.tcs.memoizer.events;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.Assert;
import org.junit.Test;

class FutureEvent extends AbstractFutureEvent<String> {
	
}

public class FutureEventTest {

	@Test 
	public void createEventTest() {
		FutureEvent ev = new FutureEvent();
	}
	
	@Test
	public void setAndGetTest() throws InterruptedException, ExecutionException {
		FutureEvent ev = new FutureEvent();
		ev.setResult("AlaMaKota");
		Assert.assertEquals(ev.getFutureResult().get(), "AlaMaKota");
	}
	
	@Test
	public void asyncTest() throws InterruptedException, ExecutionException {

		final FutureEvent ev = new FutureEvent();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(50);
					ev.setResult("OlaTez");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		
		Assert.assertEquals(ev.getFutureResult().get(), "OlaTez");
	}
	
	@Test
	public void cancelAfter() {
		final FutureEvent ev = new FutureEvent();
		Future<String> fut = ev.getFutureResult();
		ev.setResult("SweetRedhead");
		
		Assert.assertFalse(fut.cancel(true));
	}
	
}
