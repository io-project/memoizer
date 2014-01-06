package pl.edu.uj.tcs.memoizer.sharing;

import java.net.URL;

/**
 * 
 * @author Jakub Cieśla
 *
 */

public class FacebookSharer implements ISharer{
	
	private static final String name = "Share on Facebook!";
	private static final String facebookSharer = "https://www.facebook.com/sharer.php?u=";
	private final IBrowser browser = new StandardBrowser();
	
	@Override
	public String getName() {
		
		return name;
		
	}

	@Override
	public void share(URL urlToObjectToShare) {
		
		browser.openWith(facebookSharer + urlToObjectToShare.toString());
		
	}

}
