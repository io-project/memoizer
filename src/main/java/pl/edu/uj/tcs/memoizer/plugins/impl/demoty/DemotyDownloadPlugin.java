package pl.edu.uj.tcs.memoizer.plugins.impl.demoty;

import pl.edu.uj.tcs.memoizer.plugins.*;

import java.net.*;
import java.util.*;
import java.awt.Image;

/*
 * Plugin designed to downloading from Demotywatory page
 * @author pmikos (sokar92)
 */
public class DemotyDownloadPlugin implements IDownloadPlugin {
	private DemotyPropertiesSet _properties;
	
	/*
	 * Instantiates new download plugin designed
	 * for "Demotywatory.pl" page
	 */
	public DemotyDownloadPlugin(){
		_properties = new DemotyPropertiesSet();
	}
	
	@Override
	public String getName() { return "Demotywatory"; }
	
	@Override
	public Image getIcon() { return null; }
	
	@Override
	public Map<String, Object> getProperties(){
		return _properties.getMap();
	}
	
	@Override
	public void setProperties(Map<String, Object> properties){
		if(properties == null)
			throw new NullPointerException();
		
		_properties = new DemotyPropertiesSet(properties);
	}

	@Override
	public List<EViewType> getAvailableViews() {
		return null;
	}

	@Override
	public void setView(EViewType viewType) {
	}

	@Override
	public boolean hasNext(){
		return true;
	}
	
	@Override
	public Meme getNext(){
		return null;
	}
	
	@Override
	public Iterable<Meme> getNext(int n){
		return null;
	}
}