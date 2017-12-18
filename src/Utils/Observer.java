package Utils;

public interface Observer<E> {
    void notifyEvent(ListEvent<E> e);
}