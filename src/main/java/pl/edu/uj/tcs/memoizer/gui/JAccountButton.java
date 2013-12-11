package pl.edu.uj.tcs.memoizer.gui;

import javax.swing.JButton;

import pl.edu.uj.tcs.memoizer.accounts.IAccount;
import pl.edu.uj.tcs.memoizer.events.IEventService;

/**
 * Class which extends JButton. Every instance of JAccountButton holds
 * associated object od IAccount to which EventHandler provides any action.
 * IAccount is seting by contructor with second argument - instance od
 * IAccount associated with this object.
 * 
 * @author mkowalik
 * 
 */
public class JAccountButton extends JButton {
	
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
	public IAccount getAssociatedAccount() {
		return associatedAccount;
	}
}