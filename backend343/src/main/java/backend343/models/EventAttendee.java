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
public class EventAttendee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User attendee;

    @ManyToOne
    private Event event;

    private Boolean isPaid;
    private LocalDateTime registrationDate;
}
