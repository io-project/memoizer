package pl.edu.uj.tcs.memoizer.events;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import pl.edu.uj.tcs.memoizer.events.exceptions.EventException;
import pl.edu.uj.tcs.memoizer.events.exceptions.InvalidObserverException;

class SimpleEvent implements IEvent {
    public String returnMagnum() {
        return "Magnum";
    }
}

class SimpleFutureEvent extends AbstractFutureEvent<String> {

}

class SimpleEventObserver implements IEventObserver<SimpleEvent> {

    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void notify(SimpleEvent event) {
        Assert.assertEquals(event.returnMagnum(), "Magnum");
    }

    public int getCounter() {
        return counter.get();
    }
}

class SecondSimpleEventObserver implements IEventObserver<SimpleEvent> {

    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public void notify(SimpleEvent event) {
        Assert.assertEquals(event.returnMagnum(), "Magnum");
    }

    public int getCounter() {
        return counter.get();
    }
}

class SimpleFutureEventObserver implements IEventObserver<SimpleFutureEvent> {

    @Override
    public void notify(SimpleFutureEvent event) {
        try {
            Thread.sleep(50);
            event.setResult("LewakiDoPiachu");
        } catch (InterruptedException e) {
        }
    }
}

public class EventServiceTest {

    @Test
    public void createTest() {
        EventService es = new EventService();
    }

    @Test
    public void addObserverTest() throws InvalidObserverException {

        EventService es = new EventService();
        SimpleEventObserver observer = new SimpleEventObserver();
        es.attach(observer);

        Assert.assertEquals(es.listObservers().get(0), observer);
    }

    @Test
    public void detachObserverTest() throws InvalidObserverException {

        EventService es = new EventService();
        SimpleEventObserver observer = new SimpleEventObserver();
        es.attach(observer);
        Assert.assertTrue(es.detach(observer));

        Assert.assertTrue(es.listObservers().isEmpty());
    }

    @Test(expected = EventException.class)
    public void noObserverTest() throws EventException {

        EventService es = new EventService();
        SimpleEvent se = new SimpleEvent();
        es.call(se);
    }

    @Test
    public void dispatchEventTest() throws InvalidObserverException,
            EventException {

        EventService es = new EventService();
        SimpleEvent se = new SimpleEvent();
        SimpleEventObserver observer = new SimpleEventObserver();
        SecondSimpleEventObserver observer2 = new SecondSimpleEventObserver();

        es.attach(observer);
        es.attach(observer2);
        es.call(se);
        es.call(se);
    }

    @Test
    public void dispatchMultipleEventsTest() throws InvalidObserverException,
            EventException {

        EventService es = new EventService();
        SimpleEvent se = new SimpleEvent();
        SimpleEventObserver observer = new SimpleEventObserver();
        SecondSimpleEventObserver observer2 = new SecondSimpleEventObserver();

        es.attach(observer);
        es.attach(observer2);

        for (int i = 0; i < 100; i++) {
            es.call(new SimpleEvent());
        }
    }

    @Test
    public void futureEventDispatchingTest() throws InterruptedException,
            ExecutionException, EventException {

        EventService es = new EventService();
        SimpleFutureEvent fev = new SimpleFutureEvent();
        Future<String> result = fev.getFutureResult();
        SimpleFutureEventObserver observer = new SimpleFutureEventObserver();

        es.attach(observer);
        es.call(fev);

        Assert.assertNotNull(result.get());
    }
}
