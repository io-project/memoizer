package pl.edu.uj.tcs.memoizer.plugins;

import javax.naming.directory.InvalidAttributesException;

public class InvalidPlugin extends InvalidAttributesException {

	public InvalidPlugin(String message) {
		super(message);
	}
}
