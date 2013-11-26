package pl.edu.uj.tcs.memoizer.plugins;

import javax.naming.directory.InvalidAttributesException;

public class InvalidPluginException extends IllegalArgumentException {

	public InvalidPluginException(String message) {
		super(message);
	}
	
	public InvalidPluginException() {
		super();
	}
}
