package Utils;

public interface Observable<E> {

    /**
     * Register an observer.
     * @param o represents the observer
     */
    void addObserver(Observer<E> o);

    /**
     * Unregister an observer.
     * @param o represents the observer
     */
    void removeObserver(Observer<E> o);

    /**
     *  Notify all observers
     * @param event represents a list with events to be updated in observers
     */
    void notifyObservers(ListEvent<E> event);
}
