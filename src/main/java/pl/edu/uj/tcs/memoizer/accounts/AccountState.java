package pl.edu.uj.tcs.memoizer.accounts; 

import java.io.IOException;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.accounts.exceptions.ConnectionException;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.NoFileException;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.NotLoggedException;
import pl.edu.uj.tcs.memoizer.serialization.DeserializationException;
import pl.edu.uj.tcs.memoizer.serialization.IStateSink;
import pl.edu.uj.tcs.memoizer.serialization.IStateSource;
import pl.edu.uj.tcs.memoizer.serialization.SerializationException;

/**
 * Implementation of {@link IStateSource}, oraz {@link IStateSink} interfaces using cloud interface: ( {@link IAccount} class
 * @author mkowalik
 *
 */
public class AccountState implements IStateSource, IStateSink {

	private IAccount associatedAccount;
	private static Logger LOG = Logger.getLogger(AccountState.class);

	public AccountState(IAccount account) {
		this.associatedAccount = account;
	}

	@Override
	public void saveData(String object) throws SerializationException {
		byte[] bytes = object.getBytes();
		try {
			getAssociatedAccount().saveBytes(bytes, "appState.st");
		} catch (ConnectionException e) {
			LOG.error("Connection excpetion", e);
			throw new SerializationException(e);
		} catch (IOException e) {
			LOG.error("IOEexcpetion", e);
			throw new SerializationException(e);
		} catch (NotLoggedException e) {
			LOG.error("Not Logged excpetion", e);
			throw new SerializationException(e);
		}
	}

	@Override
	public String getData() throws DeserializationException {
		try {
			byte[] bytes = getAssociatedAccount().getBytes("appState.st");
			String ret = new String(bytes);
			return ret;
		} catch (ConnectionException e) {
			LOG.error("Connection excpetion", e);
			throw new DeserializationException(e);
		} catch (IOException e) {
			LOG.error("IOEexcpetion", e);
			throw new DeserializationException(e);
		} catch (NotLoggedException e) {
			LOG.error("Not Logged excpetion", e);
			throw new DeserializationException(e);
		} catch (NoFileException e) {
			return "";
		}
	}

	public IAccount getAssociatedAccount() {
		return associatedAccount;
	}

}
