package backend343.models;

import backend343.enums.EventType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Session {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    //price taken from event it's linked to

    @ManyToOne(fetch = FetchType.EAGER) //always load the schedule when loading a session for eventid
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

}
