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

    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne
    private Event event;

    @ManyToOne
    private EventSpeaker speaker;
}