package pl.edu.uj.tcs.memoizer.plugins.impl;

import pl.edu.uj.tcs.memoizer.plugins.Meme;
import java.util.Map;

public interface IMemeBuffer {
	
	boolean hasNext();
	
	Meme getNext();
	
	Iterable<Meme> getNext(int n);
}