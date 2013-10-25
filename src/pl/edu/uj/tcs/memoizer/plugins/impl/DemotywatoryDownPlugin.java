package pl.edu.uj.tcs.memoizer.plugins.impl;

import pl.edu.uj.tcs.memoizer.plugins;
import java.util.*;

/*
 * Plugin do pobierania z Demotywatorow
 * @author pmikos (sokar92)
 */
public class DemotywatoryDownPlugin : IDownloadPlugin {
	private static String _workingUrl = "www.demotywatory.pl";
	private Map<String, object> _properties;
	
	public DemotywatoryDownPlugin(){
		_properties = new Map<String, object>();
	}
	
	public String getName() { retrun "Demotywatory"; }
	
	//TO DO
	public Image getIcon() { return null; }
	
	public Map<String, object> getProperties(){
		return _properties;
	}
	
	public void setProperties(Map<String, object> properties){
		if(properties == null)
			throw new NullPointerException();
		
		_properties = properties;
	}
	
	//TO DO
	public Iterable<MemeInfo> getRecordsSinceLast(){
		return null;
	}
}