package pl.edu.uj.tcs.memoizer.gui.tabs;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.accounts.AccountsManager;
import pl.edu.uj.tcs.memoizer.accounts.IAccount;
import pl.edu.uj.tcs.memoizer.accounts.exceptions.ConnectionException;
import pl.edu.uj.tcs.memoizer.serialization.StateMap;

public class JAccountsTab extends JMemoizerTab {

	private JPanel panel = null;
	private AccountsManager accountsManager;
	private Map<IAccount, Container> displayedAccountContainers;
	private StateMap config;

	private static Logger LOG = Logger.getLogger(JAccountsTab.class);

	public JAccountsTab(String title, Icon icon, StateMap config) {
		this.title = title;
		this.icon = icon;
		this.accountsManager = AccountsManager.getInstance();
		this.displayedAccountContainers = new HashMap<IAccount, Container>();
		this.config = config;
		setPreferredSize(new Dimension(100, 100));
	}

	private void initialize() {
		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));

		for (IAccount account : accountsManager.getAvailableServices()) {
			Container newAccount = null;
			try {
				newAccount = account.prepareGUIContainer(accountsManager, this, "account:");
				panel.add(newAccount);
				displayedAccountContainers.put(account, newAccount);
			} catch (ConnectionException e) {
				LOG.error("Connection exception while trying to connect to any Accounts Service.", e);
			}
		}
	}

	@Override
	public JPanel getViewport() {
		if (panel == null)
			initialize();

		return panel;
	}

}