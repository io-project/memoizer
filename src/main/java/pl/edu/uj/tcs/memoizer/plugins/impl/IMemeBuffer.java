package pl.edu.uj.tcs.memoizer.plugins.impl;

import pl.edu.uj.tcs.memoizer.plugins.Meme;

public interface IMemeBuffer {
	
	boolean hasNext();
	
	Meme getNext();
	
	Iterable<Meme> getNext(int n);
}