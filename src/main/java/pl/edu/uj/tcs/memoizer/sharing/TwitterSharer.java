package pl.edu.uj.tcs.memoizer.sharing;

import java.net.URL;

/**
 * 
 * @author Jakub Ciesla
 *
 */

public class TwitterSharer implements ISharer {

	private static final String name = "Share on Twitter!";
	private static final String twitterSharer = "https://twitter.com/intent/tweet?url=";
	private final IBrowser browser = new StandardBrowser();

	@Override
	public String getName() {
		
		return name;
		
	}

	@Override
	public void share(URL urlToObjectToShare) {
		
		browser.openWith(twitterSharer + urlToObjectToShare.toString());

	}

}
