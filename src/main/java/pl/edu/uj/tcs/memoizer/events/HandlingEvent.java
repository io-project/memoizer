package pl.edu.uj.tcs.memoizer.events;

/**
 * 
 * @author ziemin
 * 
 *         Interface enabling event to provide a handler triggered by observer
 *         as soon as it is done with its job
 * 
 * @param <T>
 *            is a type of result processed and provided by relevant observer
 */
public interface HandlingEvent<T> extends IEvent {

    /**
     * 
     * @param result
     *            is result processed and provided by relevant observer
     */
    void handle(T result);
}
