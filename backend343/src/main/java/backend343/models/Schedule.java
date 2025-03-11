package backend343.models;

import backend343.enums.EventType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate date;
    
    @ManyToOne(fetch = FetchType.EAGER) //always load the event when loading a schedule (for eventid)
    @JoinColumn(name = "event_id")
    private Event event;

    //always load sessions so user can choose when schedule is clicked
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Session> sessions;
}
