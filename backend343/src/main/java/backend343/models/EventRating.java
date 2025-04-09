package backend343.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import lombok.Data;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rater_id")
    @JsonIgnore
    private User rater;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    @JsonIgnore
    private Event event;

    @Column(nullable = false)
    private Integer rating; // rating on a scale of 1 to 5

}
