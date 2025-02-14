package backend343.models;

import backend343.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "speakers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Speaker extends User {

    private String expertise;

    public Speaker(){}

    @OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL)
    private List<EventSpeaker> eventSpeakers;

    public Speaker(String username, String email, String password, Role role, String expertise) {
        super(username, email, password, role);
        this.expertise = expertise;
    }
}

