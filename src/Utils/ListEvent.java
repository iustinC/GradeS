package Utils;

public abstract class ListEvent<E> {

    private ListEventType type;

    public ListEvent(ListEventType type){
        this.type = type;
    }

    /**
     *
     * @return type of list of events
     */
    public ListEventType getType() {
        return type;
    }

    /**
     *  Set type of events
     * @param type represents type of events
     */
    public void setType(ListEventType type) {
        this.type = type;
    }

    /**
     * Return list of events
     * @return
     */
    public abstract Iterable<E> getList();

    /**
     * Return element from event list
     * @return
     */
    public abstract E getElement();

}
