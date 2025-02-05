package backend343.models;

import backend343.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder //simplifies object creation, good to have in spring proj
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    //not creating whole classes for diff types of users - they will be mapped to events based on role
    //your role only allows you to do certain methods, but not stop you from joining any event
    //ex. a speaker does not have to speak at an event.
    //they are only speaking at events that they are listed as EventSpeakers for
    //Eventspeaker = class dedicated to linking an event to users who WILL speak at event

    //in later stages can check for role permissions with spring security annotation
    //ex @PreAuthorize("hasRole('ADMIN')")
    @Enumerated(EnumType.STRING) //stores enum as a string in database
    private Role role;

    //an event can have many attendees, but each eventAttendee is associated to one event
    //but a user can still attend as many events they want
    //mapped by the event field in eventAttendee
    //if an event is deleted, all associated attendees in EventAttendee table will be deleted.
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventAttendee> attendees;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventSpeaker> speakers;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<EventOrganizer> organizers;

}
