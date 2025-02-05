package backend343.models;
import backend343.enums.EventType;
import backend343.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //purpose of a session is a section
    private String title;
    private String description;
    private int duration; //in minutes

    //making it so sessions are saved for certain types of events
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    //linking it to specific events
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<EventSession> eventSessions;
}
