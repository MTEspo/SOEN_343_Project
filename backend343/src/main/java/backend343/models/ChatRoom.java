package backend343.models;

import backend343.observer.ChatObservable;
import backend343.observer.ChatObserver;
import java.util.ArrayList;
import java.util.List;


public class ChatRoom implements ChatObservable{
    private final List<ChatObserver> observers = new ArrayList<>();
    private final List<String> messages = new ArrayList<>();

    @Override
    public void addObserver(ChatObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ChatObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ChatObserver observer : observers) {
            observer.update();
        }
    }

    // Method to send a message to the chatroom
    public void sendMessage(String message) {
        messages.add(message);
        notifyObservers();
    }

    public List<String> getMessages() {
        return messages;
    }

}
