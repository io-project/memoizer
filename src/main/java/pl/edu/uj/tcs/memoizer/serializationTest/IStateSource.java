package pl.edu.uj.tcs.memoizer.serializationTest;

public interface IStateSource {

	/**
	 * @return state represented as string
	 */
	String getData() throws DeserializationException;
}
