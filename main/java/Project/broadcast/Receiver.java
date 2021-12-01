package Project.broadcast;

public interface Receiver {
    default void onReceive(Object o) {}
}
