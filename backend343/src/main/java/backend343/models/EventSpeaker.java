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
@Builder
public class EventSpeaker {

    //class is only used to link speakers to events
    //speakers will only be added to a list of eventSpeakers if they are speaking at said event
    //speakers will be added to eventAttendees if they aren't speaking at that event, but still attending

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //a speaker can attend multiple events
    //many eventspeaker entries can reference the same Speaker
    @ManyToOne
    private Speaker speaker;

    //many speakers can attend one event
    //many eventspeaker entries can reference the same event
    @ManyToOne
    private Event event;

    private String topic;
}
