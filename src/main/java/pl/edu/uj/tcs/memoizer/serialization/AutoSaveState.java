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

	private IStateSink sink;
	private int lastHash;
	
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

	public AutoSaveState(String object, IStateSink sink) {
		super(object);
		init(sink);
	}
	
	public AutoSaveState(IStateSink sink) {
		init(sink);
	}
	
	private void init(IStateSink sink) {
		this.sink = sink;
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
	
	public static AutoSaveState fromSource(IStateSource source, IStateSink sink) throws DeserializationException {
		return new AutoSaveState(source.getData(), sink);
	}

	public void serialize() throws IOException {
		lastHash = jo.hashCode();
		super.serialize(sink);
	}

	@Override
	public void notify(ShutdownEvent event) {
		executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(true);
		executor.shutdown();
	}
}
