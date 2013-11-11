package pl.edu.uj.tcs.memoizer.plugins.impl;

import java.util.*;
import pl.edu.uj.tcs.memoizer.plugins.Meme;

public abstract class MemeBuffer implements IMemeBuffer {
	
	private Queue<Meme> _queue = new LinkedList<Meme>();
	
	@Override
	public final boolean hasNext(){
		return !_queue.isEmpty() || updateWaitingQueue();
	}
	
	@Override
	public final Meme getNext(){
		if(!_queue.isEmpty() || updateWaitingQueue())
			return _queue.remove();
		return null;
	}
	
	@Override
	public final Iterable<Meme> getNext(int n){
		List<Meme> lst = new ArrayList<Meme>();
		for(int i=0;i<n && hasNext();i++)
			lst.add(getNext());
		return lst;
	}
	
	/*
	 * Returns true if after update queue is NOT empty
	 */
	private boolean updateWaitingQueue(){
		Iterable<Meme> it = downloadMemes();
		for(Meme m : it) 
			_queue.add(m);
		
		return !_queue.isEmpty();
	}
	
	/*
	 * Specialied buffer should implement only this one method
	 */
	protected Iterable<Meme> downloadMemes(){
		return null;
	}
}