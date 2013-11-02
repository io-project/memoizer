package pl.edu.uj.tcs.memoizer.plugins;

import java.awt.*;
import java.util.*;

/*
 * Interface implemented by plugins
 * @author pmikos (sokar92)
 */
public interface IPlugin {
	
	/*
	 * Returns plugin name for ex. DemotywatoryDownloadPlugin
	 */
	public String getName();
	
	/*
	 * Returns Image linked with plugin (icon) 
	 */
	public Image getIcon();
	
	/*
	 * Return plugin properties map
	 */
	public Map<String, Object> getProperties();
	
	/*
	 * Sets plugin properties map
	 */
	public void setProperties(Map<String, Object> properties);
}