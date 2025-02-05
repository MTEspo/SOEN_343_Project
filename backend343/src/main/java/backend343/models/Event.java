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
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String venue;

    @Enumerated(EnumType.STRING)
    private EventType eventType; //every event has a type, can now link to a session

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventAttendee> attendees;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventSpeaker> speakers;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventSession> sessions;

    //each event belongs to an eventTrack
    @ManyToOne
    private EventTrack eventTrack;

    //because of manytomany, need to join to manage the connections between two entities
    //one event can have multiple stakeholders
    //one stakeholder can have multiple events
    @ManyToMany
    @JoinTable(
            name = "event_stakeholder", //naming the joined table
            //defines foreign key in event_stakeholder that refers to stakeholder table
            //basically this column event_id in the event_stakeholder table points to an Event
            joinColumns = @JoinColumn(name = "event_id"),
            //Defines the foreign key column in event_stakeholder that refers to the Stakeholder table
            //basically this column stakeholder_id in the event_stakeholder table points to a Stakeholder
            inverseJoinColumns = @JoinColumn(name = "stakeholder_id")
    )
    private List<Stakeholder> stakeholders;
}
