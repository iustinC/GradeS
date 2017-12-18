package Utils;

import java.util.ArrayList;
import java.util.List;

public class ObservedList <E> extends ArrayList<E> implements Observable<E>{

    protected List<Observer<E>> observers = new ArrayList<Observer<E>>();

    /**
     *  Add an observer.
     * @param o represents the observer
     */
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    /**
     *  Remove an observer.
     * @param o represents the observer
     */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /**
     *  Add an event to list of events and notify all observers
     * @param e represents event
     * @return element if was added
     *         null, else
     */
    @Override
    public boolean add(E e){
        boolean ret = super.add(e);
        ListEvent<E> event= createEvent(ListEventType.ADD, e);
        notifyObservers(event);
        return ret;
    }

    /**
     *  Update an event with a given index
     * @param index represents given index
     * @param e represents event
     * @return event if was updated
     *         null, else
     */
    @Override
    public E set(int index, E e){
        E ret = super.set(index, e);
        ListEvent<E> event= createEvent(ListEventType.UPDATE, e);
        notifyObservers(event);
        return ret;
    }

    /**
     *  Remove event from specified index
     * @param index represents given index
     * @return element if was removed
     *         null, else
     */
    @Override
    public E remove(int index){
        E ret = super.remove(index);
        ListEvent<E> event= createEvent(ListEventType.REMOVE, ret);
        notifyObservers(event);
        return ret;
    }

    /**
     *  Return a listEvent with a specified type
     * @param type represents given type
     * @param elem represents element of list
     * @return
     */
    private ListEvent<E> createEvent(ListEventType type, final E elem){
        return new ListEvent<E>(type) {
            @Override
            public ObservedList<E> getList() {
                return ObservedList.this;
            }

            @Override
            public E getElement() {
                return elem;
            }
        };
    }

    /**
     *  Notify all observers
     * @param e event for observers
     */
    public void notifyObservers(ListEvent<E> e){
        for(Observer o : observers){
            o.notifyEvent(e);
        }
    }

}
