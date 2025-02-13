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
@Table(name = "organizers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Organizer extends User {

    private String organization;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    private List<EventOrganizer> eventOrganizers;

    public Organizer(String username, String email, String password, Role role, String organization) {
        super(username, email, password, role);
        this.organization = organization;
    }
}

