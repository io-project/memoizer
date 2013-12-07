package pl.edu.uj.tcs.memoizer.accounts;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.accounts.exceptions.ConnectionException;
import pl.edu.uj.tcs.memoizer.serialization.DeserializationException;
import pl.edu.uj.tcs.memoizer.serialization.IStateSink;
import pl.edu.uj.tcs.memoizer.serialization.IStateSource;
import pl.edu.uj.tcs.memoizer.serialization.ScheduledStateSaver;
import pl.edu.uj.tcs.memoizer.serialization.SerializationException;

/**
 * Implest implementation of {@link IStateSource} and {@link IStateSink}, which returns and saves data using {@link AccountState} class, which does it in every service to which user is logged. 
 * @author mkowalik
 *
 */
public class AccountsStateManager implements IStateSource, IStateSink {
	
	private Logger LOG = Logger.getLogger(AccountsStateManager.class);
	
	private AccountsManager accountManager;
	private List<AccountState> states = new ArrayList<>();

	/**
	 * Produces {@link AccountState} instances for every account service in aplication.
	 */
	public AccountsStateManager(){
		this.accountManager = AccountsManager.getInstance();
		for (IAccount account : accountManager.getAvailableServices()){
			this.states.add(new AccountState(account));
		}
	}

	/**
	 * Saves data to every service in aplication.
	 */
	@Override
	public void saveData(String object) throws SerializationException {
		for (AccountState state : states){
			try {
				if (!state.getAssociatedAccount().isLogged()) continue;
				state.saveData(object);
			} catch (ConnectionException e) {
				LOG.error("Exception while trying to save data to cloud.", e);
				throw new SerializationException();
			}
			
		}
	}

	/**
	 * Should be taking the newest one data. Now it's taking first which is able to download. Returns null if any service is available.
	 */
	@Override
	public String getData() throws DeserializationException {
		//TODO tutaj trzeba jakoś sprytnie wybrać najnowszą??? wersję. Na razie bierze pierwszą z brzegu.
		for (AccountState state : states){
			try {
				if (!state.getAssociatedAccount().isLogged()) continue;
				return state.getData();
			} catch (ConnectionException e) {
				LOG.error("Exception while trying to get data from cloud.", e);
				throw new DeserializationException();
			}	
		}
		return null;
	}

}
