package pl.edu.uj.tcs.memoizer.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import pl.edu.uj.tcs.memoizer.events.exceptions.EventException;
import pl.edu.uj.tcs.memoizer.events.exceptions.InvalidObserverException;

/**
 * @author ziemin
 * 
 *         Simple thread-safe implementation of IEventService Events are
 *         dispatched asynchronously using cached thread pool
 */
public class EventService implements IEventService {
	
	private static Logger LOG = Logger.getLogger(EventService.class);

    private Map<Class<? extends IEvent>, List<ObserverProxy<IEventObserver<? extends IEvent>>>> eventObservers;
    private ExecutorService execService;

    public EventService() {

        eventObservers = new HashMap<>();
        execService = Executors.newCachedThreadPool();
    }

    public <T extends IEvent> boolean attach(IEventObserver<T> observer) {

        Class<? extends IEvent> clazz = getClassOfEvent(observer);

        List<ObserverProxy<IEventObserver<? extends IEvent>>> observers = getEventObservers(clazz);
        synchronized (observers) {

            try {
                observers
                        .add(new ObserverProxy<IEventObserver<? extends IEvent>>(
                                observer, "notify", clazz));
                return true;
            } catch (InvalidObserverException e) {
                return false;
            }
        }
    }

    public <T extends IEvent> boolean detach(IEventObserver<T> observer) {

        Class<? extends IEvent> clazz = getClassOfEvent(observer);

        List<ObserverProxy<IEventObserver<? extends IEvent>>> items = getEventObservers(clazz);
        synchronized (items) {

            Iterator<ObserverProxy<IEventObserver<? extends IEvent>>> it = items
                    .iterator();
            while (it.hasNext()) {

                ObserverProxy<IEventObserver<? extends IEvent>> obs = it.next();
                if (obs.getObserver().equals(observer)) {
                    it.remove();
                    return true;
                }
            }
        }

        return false;
    }

    public List<IEventObserver<? extends IEvent>> listObservers() {

        synchronized (eventObservers) {

            Collection<List<ObserverProxy<IEventObserver<? extends IEvent>>>> colls = eventObservers
                    .values();

            ArrayList<IEventObserver<? extends IEvent>> result = new ArrayList<>();
            for (List<ObserverProxy<IEventObserver<? extends IEvent>>> list : colls) {
                for (ObserverProxy<IEventObserver<? extends IEvent>> prox : list) {
                    result.add(prox.getObserver());
                }
            }

            return result;
        }
    }

    public void call(final IEvent event) throws EventException {

        List<ObserverProxy<IEventObserver<? extends IEvent>>> observers = getEventObservers(event
                .getClass());
        if (observers == null || observers.isEmpty()) {
            throw new EventException("No observer of event: " + event);
        }

        synchronized (observers) {

            for (final ObserverProxy<IEventObserver<? extends IEvent>> obs : observers) {
                execService.execute(new Runnable() {

                    @Override
                    public void run() {
                        try {

                            obs.call(event);
                        } catch (IllegalAccessException
                                | IllegalArgumentException
                                | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private List<ObserverProxy<IEventObserver<? extends IEvent>>> getEventObservers(
            Class<? extends IEvent> clazz) {

        List<ObserverProxy<IEventObserver<? extends IEvent>>> items;
        synchronized (eventObservers) {

            items = eventObservers.get(clazz);
            if (items == null) {
                items = new ArrayList<ObserverProxy<IEventObserver<? extends IEvent>>>();
                eventObservers.put(clazz, items);
            }

            return items;
        }
    }

    /**
     * This method implementation assumes that the first parameterized interface
     * is the proper one
     * @param observer
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T extends IEvent> Class<T> getClassOfEvent(
            IEventObserver<T> observer) {

        Type[] interfaces = observer.getClass().getGenericInterfaces();
        ParameterizedType type = null;
        for(int i = 0 ; i < interfaces.length; i++) {
        	try {
		        type = (ParameterizedType) interfaces[i];
		        break;
        	} catch(ClassCastException cs) {
        		LOG.debug("First Cast error: " + cs.getMessage());
        	}
        }
        if(type == null) {
			LOG.error("Type of event could not be found");
        }
        return (Class<T>) type.getActualTypeArguments()[0];
    }

}
