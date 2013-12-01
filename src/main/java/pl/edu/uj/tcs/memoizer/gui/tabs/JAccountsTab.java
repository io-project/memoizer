package pl.edu.uj.tcs.memoizer.gui.tabs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.log4j.Logger;
import org.jsoup.parser.TokenQueue;

import pl.edu.uj.tcs.memoizer.accounts.AccountsManager;
import pl.edu.uj.tcs.memoizer.accounts.IAccount;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.ConnectionException;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.NotAvailableToLogin;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.NotLoggedException;

public class JAccountsTab extends JMemoizerTab {

	JPanel panel = null;

	private static Logger LOG = Logger.getLogger(JAccountsTab.class);

	public JAccountsTab(String title, Icon icon) {
		this.title = title;
		this.icon = icon;
		setPreferredSize(new Dimension(100, 100));
	}

	/**
	 * Class which extends JButton. Every instance of JAccountButton holds
	 * associated object od IAccount to which EventHandler provides any action.
	 * IAccount is seting by contructor with second argument - instance od
	 * IAccount associated with this object.
	 * 
	 * @author mkowalik
	 * 
	 */
	private class JAccountButton extends JButton {

		private IAccount associatedAccount;

		public JAccountButton(String label, IAccount account) {
			super(label);
			this.associatedAccount = account;
		}

		/**
		 * Returns IAccount object associated with {@link JAccountButton}
		 * object.
		 * 
		 * @return IAccount object associated with {@link JAccountButton}
		 *         object.
		 */
		private IAccount getAssociatedAccount() {
			return associatedAccount;
		}
	}

	private void initialize() {
		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));

		AccountsManager manager = AccountsManager.getInstance();

		for (IAccount account : manager.getAvailableServices()) {
			Component newAccount = null;
			try {
				newAccount = prepareAccountComponent(account, "account:");
				panel.add(newAccount);
				System.out.println("DONE");
			} catch (ConnectionException e) {
				LOG.error("Connection exception while trying to connect to any Accounts Service.", e);
			}
		}
	}

	/**
	 * Prepares {@link Component} frame with all fields/buttons assiociated with account.
	 * @param account {@link IAccount} to which is associated this {@link Container}
	 * @param sufixLabel Label added after name of service describing area with all the fields.
	 * @return Returns {@link Container} containing all fields/buttons associates with account in argument.
	 * @throws ConnectionException If occours problem with connection while checking if account is logged.
	 */
	private Component prepareAccountComponent(IAccount account, String sufixLabel) throws ConnectionException {
		Container ret = new Container();
		ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));

		JLabel label = new JLabel(account.getServiceName() + " " + sufixLabel);
		ret.add(label);

		Container buttons = new Container();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		if (account.isLogged()) {
			JAccountButton saveButton = new JAccountButton("Save Settings", account);
			JAccountButton loadButton = new JAccountButton("Load Settings", account);
			JAccountButton logoutButton = new JAccountButton("Log Out", account);

			saveButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JAccountButton button = (JAccountButton) e.getSource();
					IAccount account = button.getAssociatedAccount();

					// TODO wyślij event, że trzeba zapisać plik z konfigiem
				}
			});

			loadButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JAccountButton button = (JAccountButton) e.getSource();
					IAccount account = button.getAssociatedAccount();

					// TODO wyślij event, że trzeba załądować plik z konfigiem

				}
			});

			logoutButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JAccountButton button = (JAccountButton) e.getSource();
					IAccount account = button.getAssociatedAccount();

					// TODO wyślij event, że trzeba się wylogować z chmury

				}
			});

			buttons.add(saveButton);
			buttons.add(loadButton);
			buttons.add(logoutButton);
		} else {
			JAccountButton loginButton = new JAccountButton("Log In", account);

			loginButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JAccountButton button = (JAccountButton) e.getSource();
					IAccount account = button.getAssociatedAccount();

					// TODO wyślij event, że trzeba się zalogować do chmury

				}
			});
			buttons.add(loginButton);
		}
		ret.add(buttons);

		return ret;
	}

	@Override
	public JPanel getViewport() {
		if (panel == null)
			initialize();

		return panel;
	}

}
