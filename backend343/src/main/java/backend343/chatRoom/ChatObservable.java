package backend343.chatRoom;

public interface ChatObservable {
    void addObserver(ChatObserver observer);
    void removeObserver(ChatObserver observer);
    void notifyObservers();
}