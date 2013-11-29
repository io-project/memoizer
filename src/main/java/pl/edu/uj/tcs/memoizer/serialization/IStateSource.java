package pl.edu.uj.tcs.memoizer.serialization;

public interface IStateSource {

	/**
	 * @return state represented as string
	 */
	String getData() throws DeserializationException;
}
