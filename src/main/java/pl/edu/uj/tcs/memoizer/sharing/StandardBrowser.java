package pl.edu.uj.tcs.memoizer.sharing;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 
 * @author Jakub Cieśla
 *
 */

public class StandardBrowser implements IBrowser {

	@Override
	public void openWith(String url) {

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				
				desktop.browse(new URI(url));
				
			} catch (IOException e) {
				//przerzucic wyżej ten wyjatek?
			} catch (URISyntaxException e) {
				//przerzucic wyżej ten wyjatek?
			}

		} else {
			
			Runtime runtime = Runtime.getRuntime();
			
			try {
				runtime.exec("xdg-open " + url);
			} catch (IOException e) {
				//przerzucic wyżej ten wyjatek?
			}
		}

	}

}
