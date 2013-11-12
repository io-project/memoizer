package pl.edu.uj.tcs.memoizer.plugins;

import java.awt.*;
import java.util.*;

/*
 * Interface required to implement by plugins
 * @author pmikos (sokar92)
 */
public interface IPlugin {
	
	/*
	 * Returns plugin description for ex. DemotywatoryDownloadPlugin
	 */
	public String getName();
	
	/*
	 * Returns icon related to given plugin 
	 */
	public Image getIcon();
	
	/*
	 * Returns plugin state
	 */
	public Map<String, Object> getState();
}