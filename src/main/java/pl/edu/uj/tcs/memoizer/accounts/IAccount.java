package pl.edu.uj.tcs.memoizer.accounts;

import java.awt.Container;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import pl.edu.uj.tcs.memoizer.accounts.exceptions.*;
import pl.edu.uj.tcs.memoizer.events.IEventService;
import pl.edu.uj.tcs.memoizer.gui.tabs.JAccountsTab;

/**
 * Interfejs definiujący API które musi udostępniać klasa umożliwiająca zapis konta w jakimś, implementowanym przez tą klasę, serwisie.
 * Interface defining API which have to provide class wnabling save of account in some, implementing by this class, service
 * 
 * @author Michał Kowalik (mkowalik)
 *
 */
public interface IAccount {
	
	/**
	 * Returns name of service which is used to saving account.
	 * @return Returns name of service which is used to saving account.
	 */
	public String getServiceName();
	
	/**
	 * First step of logging user to service. 
	 * @param props Properties needed to proper logging to service. Depends on service implementation.
	 * @return Properties needed to second step of logging process
	 * @throws ConnectionException When occurs problem with network connection.
	 * @throws NotAvailableToLogin When occurs any other type of problem preventing logging.
	 */
	public Properties logIn(Properties props) throws ConnectionException, NotAvailableToLogin;
	
	/**
	 * If needed, here we autenticate/authorise accounts to which we want to log in. For example, like in Dropbox it is needed to make authorisation by get token from webside which is given after request in logIn function.
	 * @param props Properties which may be needed to confirm second step of authorisation.
	 * @throws ConnectionException When occurs problem with network connection.
	 * @throws NotAvailableToLogin When occurs any other type of problem preventing logging.
	 */
	public void confirmLogin(Properties props) throws ConnectionException, NotAvailableToLogin;
	
	/**
	 * Logs out user from service.
	 * @throws ConnectionException When occurs problem with network connection.
	 */
	public void logOut() throws ConnectionException;
	
	/**
	 * Returns name of user logged to cloud or throws NotLoggedException if user is not logged in.
	 * @return Name of user logged to cloud. 
	 * @throws ConnectionException When occurs problem with network connection.
	 * @throws NotLoggedException This exception is thrown when User is not logged to service.
	 */
	public String getLogin() throws ConnectionException, NotLoggedException;
	
	/**
	 * Returns boolean value, is user logged to service.
	 * @return boolean value, true uf user is logged to service, false otherwise.
	 * @throws ConnectionException When occurs problem with network connection.
	 */
	public boolean isLogged() throws ConnectionException;
	
	/**
	 * Saves file from localhost to Cloud service. 
	 * @param fileOnLocal Reference to which is to save on cloud service account.
	 * @param pathOnCloud Path to which we want to save file on Droopbox service.
	 * @throws ConnectionException When occurs problem with network connection.
	 * @throws IOException Throws this exception when occurs any problem with file streams or any other IO problem/
	 * @throws NotLoggedException This exception is thrown when User is not logged to service.
	 */
	public void saveFile(File fileOnLocal, String pathInCloud) throws ConnectionException, IOException, NotLoggedException;
	
	/**
	 * Saves file from Cloud service to localhost
	 * @param pathOnCloud Path to file on Cloud service wanted to be saved on localhost.
	 * @param fileOnLocal Reference to file to which we want to save file from Cloud service account.
	 * @throws ConnectionException When occurs problem with network connection.
	 * @throws IOException Throws this exception when occurs any problem with file streams or any other IO problem/
	 * @throws NotLoggedException This exception is thrown when User is not logged to service.
	 * @throws NoFileException This exception is thrown when path to file on Cloud service accound is not proper.
	 */
	public void getFile(String pathInCloud, File fileOnLocal) throws ConnectionException, IOException, NotLoggedException, NoFileException;

	/**
	 * Returns Container which contains proper GUI Component for each IAccount implementation.
	 * @param accountsManager
	 * @param accountsTab 
	 * @param string
	 * @return
	 */
	public Container prepareGUIContainer(AccountsManager accountsManager, JAccountsTab accountsTab, String string)  throws ConnectionException ;
	//TODO odpowiednie wyjątki
}