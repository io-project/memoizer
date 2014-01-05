package pl.edu.uj.tcs.memoizer.sharing;

import java.net.URL;

/**
 * 
 * @author Jakub Cieśla
 *
 */

public interface ISharer {
	
	public String getName() ;
	
	public void share(URL urlToObjectToShare) ;
	
}
