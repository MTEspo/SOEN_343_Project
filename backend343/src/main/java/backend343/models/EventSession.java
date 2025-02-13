package backend343.models;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Event event;

    //Each EventSession ties a session to a specific event with event-specific details.
    @ManyToOne
    private Session session; //references reusable session

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private String location;
}
