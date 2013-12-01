package pl.edu.uj.tcs.memoizer.accounts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import pl.edu.uj.tcs.memoizer.accounts.exceptions.ConnectionException;

/**
 * Class which takes care of available Account Services. When constructing, recognizes available implementations (in pl.edu.uj.tcs.memoizer.account.impl package) of IAccount interface. It uses singleton design pattern.
 * @author mkowalik
 *
 */
public class AccountsManager {
	
	private List<IAccount> listOfAccounts = new ArrayList<>();
    private static Logger LOG = Logger.getLogger(AccountsManager.class);
    private static AccountsManager instance = null;
    
    /**
     * Constructs instance od AccountsManager, where recognizes by reflection available implementations of IAccount interface (only in pl.edu.uj.tcs.memoizer.account.impl package). 
     */
	private AccountsManager(){
		Reflections ref = new Reflections("pl.edu.uj.tcs.memoizer.accounts.impl");
		Set<Class<? extends IAccount>> accountImplementations = ref.getSubTypesOf(IAccount.class);
		for (Class<? extends IAccount> c: accountImplementations){
			if (Arrays.asList(c.getInterfaces()).contains(IAccount.class))
				try {
					listOfAccounts.add((IAccount)c.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					LOG.error("Problem with constructing instance of IAccount.", e);
				}
		}	
	}
	
	/**
	 * Class uses singleton design pattern. This method return existing instance of AccountsManager.
	 * @return existing instance of AccountsManager.
	 */
	public static AccountsManager getInstance(){
		if (instance==null) instance = new AccountsManager();
		return instance;
	}
	
	/**
	 * Return list of available services.
	 * @return List of available services.
	 */
	public List<IAccount> getAvailableServices(){
		return listOfAccounts;
	}
	
	/*
	 * List of available services, which method isLogged() returns true value at moment of invokation of getLoggedServices() function.
	 */
	public List<IAccount> getLoggedServices() throws ConnectionException{
		List<IAccount> list = new ArrayList<IAccount>();
		for (IAccount account: listOfAccounts){
			if (account.isLogged()) list.add(account);
		}
		return list;
	}
}
