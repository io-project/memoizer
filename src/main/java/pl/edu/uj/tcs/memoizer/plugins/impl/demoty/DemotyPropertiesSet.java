package pl.edu.uj.tcs.memoizer.plugins.impl.demoty;

import java.util.Map;
import java.util.HashMap;

class DemotyPropertiesSet {
	private Map<String, Object> _map;
	
	DemotyPropertiesSet(){
		_map = new HashMap<String, Object>();
	}
	
	DemotyPropertiesSet(Map<String, Object> map){
		_map = map;
	}
	
	Map<String, Object> getMap(){
		return _map;
	}
}