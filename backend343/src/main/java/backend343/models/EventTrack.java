package backend343.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; //examples are like "AI & Machine Learning", "Cybersecurity", and have all events

    //each eventTrack has multiple events
    //list of events in this track
//    @OneToMany(mappedBy = "eventTrack", cascade = CascadeType.ALL)
//    private List<Event> events;
}
