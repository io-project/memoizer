package pl.edu.uj.tcs.memoizer.plugins.communication.mocks;

import java.util.concurrent.atomic.AtomicInteger;

import pl.edu.uj.tcs.memoizer.handlers.IHandler;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public class GuiHandlerMock implements IHandler<Meme> {
	
	private AtomicInteger counter = new AtomicInteger(0);
	
	public int getHandledCount() {
		return counter.get();
	}

	@Override
	public void handle(Meme t) {
		counter.incrementAndGet();
	}

}
