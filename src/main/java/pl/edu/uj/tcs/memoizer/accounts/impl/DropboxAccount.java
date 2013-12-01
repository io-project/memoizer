package pl.edu.uj.tcs.memoizer.accounts.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jsoup.safety.Cleaner;

import pl.edu.uj.tcs.memoizer.accounts.IAccount;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.ConnectionException;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.NoFileException;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.NotAvailableToLogin;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.NotLoggedException;
import pl.edu.uj.tcs.memoizer.events.EventService;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;

/**
 * Implementation of accounts for Dropbox service
 * @author Michał Kowalik (mkowalik)
 *
 */
public class DropboxAccount implements IAccount {

	private final String serviceName = "Dropbox";
    private final String APP_KEY = "8m2lt0i7m8sj3bv";
    private final String APP_SECRET = "kkam9jksopnf1jb";
    private DbxClient client = null; 
    private DbxWebAuthNoRedirect webAuth = null;
    private DbxRequestConfig config = null;
    
    private static Logger LOG = Logger.getLogger(DropboxAccount.class);
	
	@Override
	public String getServiceName() {
		return serviceName;
	}


	/**
	 * This is method used to first (of two) steps needed to log in to Dropbox. Properties from argument are ignored. In returned Properties object there is key "authUrl", and value cosiders to adress to which is needed to go and copy autorisation token which is delivered in second step - running method: confirmLogin with proper argument. It is needed to know, every token has it own timeout, so it is needed to care about NotLogged exceptions.
	 * @param props Properties Ignored.
	 * @return Properties with set proper value for key "authUrl". Adres neede to visit for taking token for second step of authorisation.
	 */
	@Override
	public Properties logIn(Properties props) throws ConnectionException,
			NotAvailableToLogin {
		
		DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
		
        config = new DbxRequestConfig("Memoizer/1.0", Locale.getDefault().toString());
        webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        
        String authorizeUrl;
        try {
        	authorizeUrl = webAuth.start();
        } catch (Exception e) {
        	LOG.error("Problem with starting autohorisation to Dropbox.", e);
        	throw new NotAvailableToLogin();
        }
        Properties ret = new Properties();
        ret.put("authUrl", authorizeUrl);
        
        return ret;
	}

	/**
	 * Second step needed to provide token taken from webside provided from first step of logging ( logIn() ).
	 * @param props {@link Properties} Dictionary which have proper token taken from Dropbox site (url received in returned object from logIn method) under "authKey" key.
	 */
	@Override
	public void confirmLogin(Properties props) throws ConnectionException,
			NotAvailableToLogin {
		
		if (!props.containsKey("authKey")) throw new NotAvailableToLogin();
		if (webAuth == null) throw new NotAvailableToLogin();
		
		String token = props.getProperty("authKey");
		
		try{
			DbxAuthFinish authFinish = webAuth.finish(token);
			client = new DbxClient(config, authFinish.accessToken);
		} catch (DbxException.NetworkIO | DbxException.ProtocolError | DbxException.RetryLater | DbxException.ServerError e) {
        	LOG.error("Any network exception during confirming authorisation to Dropbox.", e);
			throw new ConnectionException();
		} catch (DbxException e){
			LOG.error("Any problem during confirming authorisation to Dropbox.", e);
			throw new NotAvailableToLogin();
		} 
        
	}

	@Override
	public void logOut() {
	    client = null; 
	    webAuth = null;
	    config = null;
	}

	@Override
	public boolean isLogged() throws ConnectionException {
		if (client==null) return false;
		return true;
	}


	@Override
	public void saveFile(File fileOnLocal, String pathInCloud)
			throws ConnectionException, IOException, NotLoggedException {
		if (!isLogged()) throw new NotLoggedException();
		InputStream stream = new FileInputStream(fileOnLocal);
		try {
			client.uploadFile(pathInCloud, DbxWriteMode.add(), stream.available(), stream);
		} catch (DbxException.InvalidAccessToken e) {
        	LOG.info("Invalid Acces Token when trying to save file to Dropbox.", e);
			throw new NotLoggedException();
		} catch (DbxException.NetworkIO | DbxException.ProtocolError | DbxException.RetryLater | DbxException.ServerError e) {
			LOG.error("Any network exception during confirming authorisation to Dropbox.", e);
			throw new ConnectionException();
		} catch (DbxException e){
			LOG.error("Any problem with saving file to Dropbox.", e);
			throw new IOException();
		} finally {
			stream.close();
		}
	}


	@Override
	public void getFile(String pathInCloud, File fileOnLocal)
			throws ConnectionException, IOException, NotLoggedException,
			NoFileException {
		if (!isLogged()) throw new NotLoggedException();
		OutputStream stream = new FileOutputStream(fileOnLocal);
		try {
			if (client.getMetadata(pathInCloud)==null) throw new NoFileException();
			client.getFile(pathInCloud, null, stream);
		} catch (DbxException.InvalidAccessToken e) {
        	LOG.info("Invalid Acces Token when trying to download file from Dropbox.", e);
			throw new NotLoggedException();
		} catch (DbxException.NetworkIO | DbxException.ProtocolError | DbxException.RetryLater | DbxException.ServerError e) {
			LOG.error("Any network exception during confirming authorisation to Dropbox.", e);
			throw new ConnectionException();
		} catch (DbxException e){
			LOG.error("Any problem with saving file to Dropbox.", e);
			throw new IOException();
		} finally {
			stream.close();
		}
	}


	@Override
	public String getLogin() throws ConnectionException, NotLoggedException {
		if (!isLogged() || client==null) throw new NotLoggedException();
		try {
			return client.getAccountInfo().displayName;
		} catch (DbxException.InvalidAccessToken e) {
        	LOG.info("Invalid Acces Token when trying to download file from Dropbox.", e);
			throw new NotLoggedException();
		} catch (DbxException e){
			LOG.error("Any network exception during confirming authorisation to Dropbox.", e);
			throw new ConnectionException();
		} 
	}

}
