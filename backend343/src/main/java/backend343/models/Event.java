package backend343.models;

import backend343.enums.EventType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private EventType type;
    private BigDecimal price;

    //made fetch type eager bc all schedules associated to event will be loaded every time
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Schedule> schedules;
}
