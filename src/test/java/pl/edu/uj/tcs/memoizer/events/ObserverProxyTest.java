package pl.edu.uj.tcs.memoizer.events;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import pl.edu.uj.tcs.memoizer.events.exceptions.InvalidObserverException;

class DumbTestClass {
	
	public void print(String randomWord, String evenMoreRandomWord) {
		System.out.println("Printing some super random words: " + randomWord + " " + evenMoreRandomWord);
	}
}

public class ObserverProxyTest {

	@Test
	public void createObserverProxyTest() throws InvalidObserverException {
		DumbTestClass dumb = new DumbTestClass();
		
		ObserverProxy<DumbTestClass> pr = new ObserverProxy<DumbTestClass>(dumb, "print", new Class<?>[]{String.class, String.class});
	}
	
	@Test(expected=InvalidObserverException.class)
	public void failObserverProxyTest() throws InvalidObserverException {
		ObserverProxy<DumbTestClass> pr = new ObserverProxy<DumbTestClass>(new DumbTestClass(), "print", new Class<?>[]{String.class, Integer.class});
	}
	
	@Test
	public void callObserverProxyTest() throws InvalidObserverException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ObserverProxy<DumbTestClass> pr = new ObserverProxy<DumbTestClass>(new DumbTestClass(), "print", new Class<?>[]{String.class, String.class});
		pr.call("Pig", "Destroyer");
	}
	
}
