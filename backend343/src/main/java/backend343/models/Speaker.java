package backend343.models;

import backend343.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "speakers")
@PrimaryKeyJoinColumn(name = "user_id") // Uses the same ID as User
public class Speaker extends User {

    private String expertise;

    @OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL)
    private List<EventSpeaker> eventSpeakers;

    public Speaker(String username, String email, String password, Role role, String expertise) {
        super(username, email, password, role);
        this.expertise = expertise;
    }
}

