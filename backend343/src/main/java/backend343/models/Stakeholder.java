package backend343.models;

import backend343.enums.StakeholderType;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stakeholder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private StakeholderType stakeholderType;

    //bidirectional manytomany
    //this is the other side of the relationship
    //the joining of tables happens in Event class
//    @ManyToMany(mappedBy = "stakeholders")
//    private List<Event> events;
}