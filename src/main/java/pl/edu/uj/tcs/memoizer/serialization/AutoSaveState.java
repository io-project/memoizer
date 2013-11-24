package pl.edu.uj.tcs.memoizer.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.events.IEventObserver;
import pl.edu.uj.tcs.memoizer.runtime.ShutdownEvent;

public class AutoSaveState extends StateObject implements IEventObserver<ShutdownEvent> {
	
	Logger LOG = Logger.getLogger(AutoSaveState.class);

	private int SAVE_RATE_MS = 6000;

	private OutputStream os;
	private int lastHash;
	
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

	public AutoSaveState(String object, OutputStream os) {
		super(object);
		init(os);
	}
	
	public AutoSaveState(OutputStream os) {
		init(os);
	}
	
	private void init(OutputStream os) {
		this.os = os;
		lastHash = jo.hashCode();
		
		executor.scheduleAtFixedRate(new Saver(), 0, SAVE_RATE_MS, TimeUnit.MILLISECONDS);
	}
	
	public void changeSaveRate(int milis) {

		SAVE_RATE_MS = milis;
		executor.shutdown();
		executor = new ScheduledThreadPoolExecutor(1);
		executor.scheduleAtFixedRate(new Saver(), 0, SAVE_RATE_MS, TimeUnit.MILLISECONDS);
	}
	
	private class Saver implements Runnable {
		@Override
		public void run() {
			if(lastHash != jo.hashCode()) {
				try {
					AutoSaveState.this.serialize();
				} catch (IOException e) {
					LOG.error("Could not serialize state: " + e.getMessage());
				}
			}
		}
	}
	
	public static AutoSaveState fromStream(InputStream is, OutputStream os) throws IOException {
		return new AutoSaveState(IOUtils.toString(is), os);
	}

	public void serialize() throws IOException {
		lastHash = jo.hashCode();
		super.serialize(os);
	}

	@Override
	public void notify(ShutdownEvent event) {
		executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
		executor.shutdown();
	}
}
