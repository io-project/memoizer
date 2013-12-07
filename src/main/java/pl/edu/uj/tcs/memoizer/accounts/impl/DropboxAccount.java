package pl.edu.uj.tcs.memoizer.accounts.impl;

import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.accounts.AccountsManager;
import pl.edu.uj.tcs.memoizer.accounts.IAccount;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.ConnectionException;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.NoFileException;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.NotAvailableToLogin;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.NotLoggedException;
import pl.edu.uj.tcs.memoizer.gui.JAccountButton;
import pl.edu.uj.tcs.memoizer.gui.tabs.JAccountsTab;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.DbxWriteMode;
import com.google.common.collect.Sets.SetView;

/**
 * Implementation of accounts for Dropbox service
 * 
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
	 * This is method used to first (of two) steps needed to log in to Dropbox.
	 * Properties from argument are ignored. In returned Properties object there
	 * is key "authUrl", and value cosiders to adress to which is needed to go
	 * and copy autorisation token which is delivered in second step - running
	 * method: confirmLogin with proper argument. It is needed to know, every
	 * token has it own timeout, so it is needed to care about NotLogged
	 * exceptions.
	 * 
	 * @param props
	 *            Properties Ignored.
	 * @return Properties with set proper value for key "authUrl". Adres neede
	 *         to visit for taking token for second step of authorisation.
	 */
	@Override
	public Properties logIn(Properties props) throws ConnectionException, NotAvailableToLogin {

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
	 * Second step needed to provide token taken from webside provided from
	 * first step of logging ( logIn() ).
	 * 
	 * @param props
	 *            {@link Properties} Dictionary which have proper token taken
	 *            from Dropbox site (url received in returned object from logIn
	 *            method) under "authKey" key.
	 */
	@Override
	public void confirmLogin(Properties props) throws ConnectionException, NotAvailableToLogin {

		if (!props.containsKey("authKey"))
			throw new NotAvailableToLogin();
		if (webAuth == null)
			throw new NotAvailableToLogin();

		String token = props.getProperty("authKey");

		try {
			DbxAuthFinish authFinish = webAuth.finish(token);
			client = new DbxClient(config, authFinish.accessToken);
		} catch (DbxException.NetworkIO | DbxException.ProtocolError | DbxException.RetryLater | DbxException.ServerError e) {
			LOG.error("Any network exception during confirming authorisation to Dropbox.", e);
			throw new ConnectionException();
		} catch (DbxException e) {
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
		if (client == null)
			return false;
		return true;
	}

	@Override
	public void saveFile(File fileOnLocal, String pathInCloud) throws ConnectionException, IOException, NotLoggedException {
		if (!isLogged())
			throw new NotLoggedException();
		InputStream stream = new FileInputStream(fileOnLocal);
		try {
			client.uploadFile(pathInCloud, DbxWriteMode.add(), stream.available(), stream);
		} catch (DbxException.InvalidAccessToken e) {
			LOG.info("Invalid Acces Token when trying to save file to Dropbox.", e);
			throw new NotLoggedException();
		} catch (DbxException.NetworkIO | DbxException.ProtocolError | DbxException.RetryLater | DbxException.ServerError e) {
			LOG.error("Any network exception during confirming authorisation to Dropbox.", e);
			throw new ConnectionException();
		} catch (DbxException e) {
			LOG.error("Any problem with saving file to Dropbox.", e);
			throw new IOException();
		} finally {
			stream.close();
		}
	}

	@Override
	public void saveBytes(byte[] bytesToSave, String pathInCloud) throws ConnectionException, IOException, NotLoggedException {
		if (!isLogged())
			throw new NotLoggedException();
		InputStream stream = new ByteArrayInputStream(bytesToSave);
		try {
			client.uploadFile(pathInCloud, DbxWriteMode.add(), stream.available(), stream);
		} catch (DbxException.InvalidAccessToken e) {
			LOG.info("Invalid Acces Token when trying to save file to Dropbox.", e);
			throw new NotLoggedException();
		} catch (DbxException.NetworkIO | DbxException.ProtocolError | DbxException.RetryLater | DbxException.ServerError e) {
			LOG.error("Any network exception during confirming authorisation to Dropbox.", e);
			throw new ConnectionException();
		} catch (DbxException e) {
			LOG.error("Any problem with saving file to Dropbox.", e);
			throw new IOException();
		} finally {
			stream.close();
		}

	}

	@Override
	public void getFile(String pathInCloud, File fileOnLocal) throws ConnectionException, IOException, NotLoggedException, NoFileException {
		if (!isLogged())
			throw new NotLoggedException();
		OutputStream stream = new FileOutputStream(fileOnLocal);
		try {
			if (client.getMetadata(pathInCloud) == null)
				throw new NoFileException();
			client.getFile(pathInCloud, null, stream);
		} catch (DbxException.InvalidAccessToken e) {
			LOG.info("Invalid Acces Token when trying to download file from Dropbox.", e);
			throw new NotLoggedException();
		} catch (DbxException.NetworkIO | DbxException.ProtocolError | DbxException.RetryLater | DbxException.ServerError e) {
			LOG.error("Any network exception during confirming authorisation to Dropbox.", e);
			throw new ConnectionException();
		} catch (DbxException e) {
			LOG.error("Any problem with saving file to Dropbox.", e);
			throw new IOException();
		} finally {
			stream.close();
		}
	}

	@Override
	public byte[] getBytes(String pathInCloud) throws ConnectionException, IOException, NotLoggedException, NoFileException {
		if (!isLogged())
			throw new NotLoggedException();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			if (client.getMetadata(pathInCloud) == null)
				throw new NoFileException();
			client.getFile(pathInCloud, null, stream);
		} catch (DbxException.InvalidAccessToken e) {
			LOG.info("Invalid Acces Token when trying to download file from Dropbox.", e);
			throw new NotLoggedException();
		} catch (DbxException.NetworkIO | DbxException.ProtocolError | DbxException.RetryLater | DbxException.ServerError e) {
			LOG.error("Any network exception during confirming authorisation to Dropbox.", e);
			throw new ConnectionException();
		} catch (DbxException e) {
			LOG.error("Any problem with saving file to Dropbox.", e);
			throw new IOException();
		} finally {
			stream.close();
		}
		return stream.toByteArray();
	}

	@Override
	public String getLogin() throws ConnectionException, NotLoggedException {
		if (!isLogged() || client == null)
			throw new NotLoggedException();
		try {
			return client.getAccountInfo().displayName;
		} catch (DbxException.InvalidAccessToken e) {
			LOG.info("Invalid Acces Token when trying to download file from Dropbox.", e);
			throw new NotLoggedException();
		} catch (DbxException e) {
			LOG.error("Any network exception during confirming authorisation to Dropbox.", e);
			throw new ConnectionException();
		}
	}

	@Override
	public Container prepareGUIContainer(AccountsManager accountsManager, final JAccountsTab tab, String sufixLabel) throws ConnectionException {
		final Container ret = new Container();
		ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));

		JLabel titleLabel = new JLabel(this.getServiceName() + " " + sufixLabel);
		ret.add(titleLabel);

		final Container loggedContainer = new Container();
		loggedContainer.setLayout(new BoxLayout(loggedContainer, BoxLayout.Y_AXIS));

		final JLabel statusLabel = new JLabel();
		loggedContainer.add(statusLabel);

		final Container notLoggedContainer = new Container();
		notLoggedContainer.setLayout(new BoxLayout(notLoggedContainer, BoxLayout.Y_AXIS));

		final Container firsStepContainer = new Container();
		firsStepContainer.setLayout(new BoxLayout(firsStepContainer, BoxLayout.Y_AXIS));

		final Container secondStepContainer = new Container();
		secondStepContainer.setLayout(new BoxLayout(secondStepContainer, BoxLayout.Y_AXIS));

		final JLabel labelPlaceholder = new JLabel();

		final JTextField authTokenPlaceholder = new JTextField();
		authTokenPlaceholder.setMaximumSize(new Dimension(1000, 20));

		final JAccountButton confirmButton = new JAccountButton("Confirm", this);
		final JAccountButton loginButton = new JAccountButton("Log In", this);
		final JAccountButton logoutButton = new JAccountButton("Log Out", this);

		/* LOGOUT BUTTON ACTIONS */
		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JAccountButton button = (JAccountButton) e.getSource();
				IAccount account = button.getAssociatedAccount();

				try {
					account.logOut();
				} catch (ConnectionException e1) {
					JOptionPane.showMessageDialog(tab, "Not available to connect to Dropbox cloud.", "Connection error", JOptionPane.ERROR_MESSAGE);
					LOG.error("Connection Exception while trying to Log Out from Dropbox.", e1);
				}

				resetVisability(statusLabel, loggedContainer, notLoggedContainer, secondStepContainer, labelPlaceholder, authTokenPlaceholder);

			}
		});

		loggedContainer.add(logoutButton);

		/* CONFIRM BUTTON ACTION */
		confirmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JAccountButton button = (JAccountButton) e.getSource();
				final IAccount account = button.getAssociatedAccount();

				String authToken = authTokenPlaceholder.getText();
				if (authToken.equals("")) {
					JOptionPane.showMessageDialog(tab, "Insert autorisation token from Dropbox website!", "Authorisation", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Properties props = new Properties();
				props.put("authKey", authToken);
				try {
					account.confirmLogin(props);
					statusLabel.setText("Logged as: " + account.getLogin());
					tab.revalidate();
					notLoggedContainer.setVisible(false);
					statusLabel.setVisible(true);
					loggedContainer.setVisible(true);
				} catch (ConnectionException e1) {
					JOptionPane.showMessageDialog(tab, "Not available to connect to Dropbox cloud.", "Connection error", JOptionPane.ERROR_MESSAGE);
					LOG.error("Connection Exception while trying to Log In to Dropbox.", e1);
				} catch (NotAvailableToLogin e1) {
					JOptionPane.showMessageDialog(tab, "Not available to log in to Dropbox cloud.", "Logging error", JOptionPane.ERROR_MESSAGE);
					LOG.error("Not available to log in while trying to Log In to Dropbox.", e1);
				} catch (NotLoggedException e1) {
					JOptionPane.showMessageDialog(tab, "Internal error.", "Error", JOptionPane.ERROR_MESSAGE);
					LOG.error("Problem while truing to get login of recently logged to Dropbox.", e1);
				}

			}
		});

		secondStepContainer.add(labelPlaceholder);
		secondStepContainer.add(authTokenPlaceholder);
		secondStepContainer.add(confirmButton);

		/* LOGIN BUTTON ACTION */
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JAccountButton button = (JAccountButton) e.getSource();
				final IAccount account = button.getAssociatedAccount();

				Properties props;
				try {
					props = account.logIn(new Properties());
					String authUrl = props.getProperty("authUrl");
					if (Desktop.isDesktopSupported()) {
						Desktop.getDesktop().browse(new URI(authUrl));
						labelPlaceholder.setText("Insert authorisation token:");
					} else {
						labelPlaceholder.setText("Go to URL: " + authUrl);
					}
					authTokenPlaceholder.setText("");
					secondStepContainer.setVisible(true);
					tab.invalidate();
				} catch (ConnectionException e1) {
					JOptionPane.showMessageDialog(tab, "Not available to connect to Dropbox cloud.", "Connection error", JOptionPane.ERROR_MESSAGE);
					LOG.error("Connection Exception while trying to Log In to Dropbox.", e1);
				} catch (NotAvailableToLogin e1) {
					JOptionPane.showMessageDialog(tab, "Not available to log in to Dropbox cloud.", "Logging error", JOptionPane.ERROR_MESSAGE);
					LOG.error("Not available to log in while trying to Log In to Dropbox.", e1);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(tab, "Internal error.", "Error", JOptionPane.ERROR_MESSAGE);
					LOG.error("IOExeption while trying to Log In to Dropbox.", e1);
				} catch (URISyntaxException e1) {
					JOptionPane.showMessageDialog(tab, "Internal error.", "Error", JOptionPane.ERROR_MESSAGE);
					LOG.error("URISyntaxException while trying to Log In to Dropbox.", e1);
				}

			}
		});

		ret.add(loginButton);

		firsStepContainer.add(loginButton);

		notLoggedContainer.add(firsStepContainer);
		notLoggedContainer.add(secondStepContainer);

		ret.add(loggedContainer);
		ret.add(notLoggedContainer);
		
		resetVisability(statusLabel, loggedContainer, notLoggedContainer, secondStepContainer, labelPlaceholder, authTokenPlaceholder);

		loggedContainer.setVisible(this.isLogged());
		notLoggedContainer.setVisible(!this.isLogged());


		return ret;
	}

	public void resetVisability(JLabel statusLabel, Container loggedContainer, Container notLoggedContainer, Container secondStepContainer,
			JLabel labelPlaceholder, JTextField authTokenPlaceholder) {
		statusLabel.setVisible(false);
		loggedContainer.setVisible(false);
		notLoggedContainer.setVisible(true);
		secondStepContainer.setVisible(false);
		labelPlaceholder.setText("");
		authTokenPlaceholder.setText("");
	}

}