package pl.edu.uj.tcs.memoizer.serializationTest;

import java.io.IOException;

public class DeserializationException extends IOException {

	public DeserializationException() {
		super();
	}

	public DeserializationException(String message, Throwable cause) {
		super(message, cause);
	}

	public DeserializationException(String message) {
		super(message);
	}

	public DeserializationException(Throwable cause) {
		super(cause);
	}
}
