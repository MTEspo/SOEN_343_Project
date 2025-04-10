package backend343.chatRoom;
import backend343.models.User;
import backend343.repository.ChatRoomRepository;
import backend343.service.TicketService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom implements ChatObservable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "chatroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "chatroom_users",
            joinColumns = @JoinColumn(name = "chatroom_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users= new ArrayList<>();

    @Transient
    private final List<ChatObserver> observers = new ArrayList<>();

    @Override
    public void addObserver(ChatObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ChatObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Long chatroomId, Long senderId) {
        //did this to not instantiate chatroomservice inside model class
        ChatroomService chatroomService = ApplicationContextProvider.getBean(ChatroomService.class);
        chatroomService.notifyObservers(chatroomId, senderId);
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyObservers(this.id, message.getSender().getId());
    }


}

