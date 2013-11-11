package pl.edu.uj.tcs.memoizer.plugins.impl.demoty;

import pl.edu.uj.tcs.memoizer.plugins.*;
import pl.edu.uj.tcs.memoizer.plugins.impl.*;

import java.net.*;
import java.util.*;
import java.lang.IllegalArgumentException;
import java.awt.Image;

/*
 * Plugin designed to downloading from Demotywatory page
 * @author pmikos (sokar92)
 */
public final class DemotyDownloadPlugin implements IDownloadPlugin {
	private DemotyPropertiesSet _properties;
	private EViewType _view;
	private Map<EViewType, IMemeBuffer> _viewBuffer;
	
	/*
	 * Instantiates new download plugin designed
	 * for "Demotywatory.pl" page
	 */
	public DemotyDownloadPlugin(){		
		_properties = new DemotyPropertiesSet();
		_view = null;
		
		_viewBuffer = new HashMap<EViewType, IMemeBuffer>();
		_viewBuffer.put(EViewType.CHRONOLOGICAL, new DemotyMemeBufferChrono());
		_viewBuffer.put(EViewType.UNSEEN, new DemotyMemeBufferUnseen());
		_viewBuffer.put(EViewType.FAVOURITE, new DemotyMemeBufferTop());
		_viewBuffer.put(EViewType.QUEUE, new DemotyMemeBufferQueue());
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
		List<EViewType> list = new ArrayList<EViewType>();
		list.add(EViewType.CHRONOLOGICAL);
		list.add(EViewType.FAVOURITE);
		list.add(EViewType.UNSEEN);
		list.add(EViewType.QUEUE);
		return list;
	}

	@Override
	public void setView(EViewType viewType) {
		if(!getAvailableViews().contains(viewType))
			throw new IllegalArgumentException(
					"Demoty plugin does not support " + viewType.toString() + " view!");
		
		_view = viewType;
	}

	@Override
	public boolean hasNext(){
		if(_view == null)
			return false;
		return _viewBuffer.get(_view).hasNext();
	}
	
	@Override
	public Meme getNext(){
		if(_view == null)
			return null;
		return _viewBuffer.get(_view).getNext();
	}
	
	@Override
	public Iterable<Meme> getNext(int n){
		if(_view == null)
			return null;
		return _viewBuffer.get(_view).getNext(n);
	}
}