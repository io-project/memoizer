package pl.edu.uj.tcs.memoizer.plugins.impl.demoty;

import pl.edu.uj.tcs.memoizer.plugins.Meme;
import pl.edu.uj.tcs.memoizer.plugins.impl.MemeBuffer;

public class DemotyMemeBufferUnseen extends MemeBuffer {
	
	@Override
	protected Iterable<Meme> downloadMemes(){
		return null;
	}
}