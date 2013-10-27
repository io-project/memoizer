package pl.edu.uj.tcs.memoizer.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import pl.edu.uj.tcs.memoizer.events.exceptions.InvalidObserverException;

/**
 * @author ziemin
 * 
 *         Wrapper over single method from object
 * 
 * @param <T>
 *            is an object of which method is proxied by this class
 */
public class ObserverProxy<T> {

    private T observer;
    private Method method;

    /**
     * Constructor simply calls getDeclaredMethod on a class of provided
     * observer
     * 
     * @param observer
     * @param methodName
     * @param classes
     *            are arguments identifying method
     * @throws InvalidObserverException
     *             if provided arguments are not valid for given object
     */
    public ObserverProxy(T observer, String methodName, Class<?>... classes)
            throws InvalidObserverException {

        this.observer = observer;
        try {
            method = observer.getClass().getDeclaredMethod(methodName, classes);
        } catch (NoSuchMethodException | SecurityException e) {
            throw new InvalidObserverException("Invalid method for observer: "
                    + methodName);
        }
    }

    /**
     * @returns wrapped observer
     */
    public T getObserver() {
        return observer;
    }

    /**
     * calls wrapped method
     */
    public void call(Object... args) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        method.invoke(observer, args);
    }
}
