package pl.edu.uj.tcs.memoizer.plugins.impl;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import pl.edu.uj.tcs.memoizer.plugins.IDownloadPlugin;
import pl.edu.uj.tcs.memoizer.plugins.MemeInfo;

/*
 * Plugin do pobierania z Demotywatorow
 * @author pmikos (sokar92)
 */
public class DemotywatoryDownPlugin implements IDownloadPlugin {
	private static String _workingUrl = "www.demotywatory.pl";
	private Map<String, Object> _properties;
	
	public DemotywatoryDownPlugin(){
		_properties = new HashMap<String, Object>();
	}
	
	public String getName() { return "Demotywatory"; }
	
	//TO DO
	public Image getIcon() { return null; }
	
	public Map<String, Object> getProperties(){
		return _properties;
	}
	
	public void setProperties(Map<String, Object> properties){
		if(properties == null)
			throw new NullPointerException();
		
		_properties = properties;
	}
	
	//TO DO
	public Iterable<MemeInfo> getRecordsSinceLast(){
		return null;
	}
}