package pl.edu.uj.tcs.memoizer.serialization;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONObject;
import pl.edu.uj.tcs.memoizer.events.IEventObserver;

public class StateMap implements IEventObserver<SaveStateEvent>, Map<String, IStateObject> {
	
	private IStateSink sink;
	private Map<String, IStateObject> statesMap = new HashMap<String, IStateObject>();

	public StateMap(IStateSource stateSource, IStateSink sink) {

		this.sink = sink;
		buildMap(stateSource.getData());
	}
	
	public StateMap(IStateSink sink) {
		this.sink = sink;
	}
	
	private void buildMap(String object) {
		
		JSONObject jo = JSONObject.fromObject(object);

		@SuppressWarnings("unchecked")
		Iterator<String> it = (Iterator<String>) jo.keys();
		while(it.hasNext()) {
			String key = it.next();
			JSONObject subjo = jo.getJSONObject(key);
			statesMap.put(key, new StateObject(subjo));
		}
	}
	
	@Override
	public boolean isEmpty() {
		
		synchronized (statesMap) {
			return statesMap.isEmpty();
		}
	}
	
	@Override
	public IStateObject put(String key, IStateObject value) {
		
		synchronized (statesMap) {
			return statesMap.put(key, value);
		}
	}
	
	@Override
	public void clear() {

		synchronized (statesMap) {
			statesMap.clear();
		}
	}

	@Override
	public boolean containsKey(Object key) {

		synchronized (statesMap) {
			return statesMap.containsKey(key);
		}
	}

	@Override
	public boolean containsValue(Object value) {

		synchronized (statesMap) {
			for(IStateObject so: statesMap.values()) {
				if(so.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Set<Entry<String, IStateObject>> entrySet() {

		synchronized (statesMap) {
			return statesMap.entrySet();
		}
	}

	@Override
	public IStateObject get(Object key) {

		synchronized (statesMap) {

			if(statesMap.containsKey(key)) {

				return statesMap.get(key);
			} else {
				IStateObject so = new StateObject();
				statesMap.put((String) key, so);
				return so;
			}
		}
	}

	@Override
	public Set<String> keySet() {
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends IStateObject> m) {

		synchronized (statesMap) {
			statesMap.putAll(m);
		}
	}

	@Override
	public IStateObject remove(Object key) {

		synchronized (statesMap) {
			return statesMap.remove(key);
		}
	}

	@Override
	public int size() {
		
		synchronized (statesMap) {
			return statesMap.size();
		}
	}

	@Override
	public Collection<IStateObject> values() {
		
		synchronized (statesMap) {
			return statesMap.values();
		}
	}

	public void save() {
		save(sink);
	}
	
	public synchronized void save(IStateSink otherSink) {

		synchronized(statesMap) {
			JSONObject jo = new JSONObject();
			
			for(Entry<String, IStateObject> entry: statesMap.entrySet()) {
				jo.put(entry.getKey(), entry.getValue().getJSON());
			}
			otherSink.saveData(jo.toString());
		}
	}

	@Override
	public void notify(SaveStateEvent event) {
		save();
	}
}