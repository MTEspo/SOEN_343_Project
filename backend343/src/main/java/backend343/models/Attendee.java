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
@Table(name = "attendees")
@PrimaryKeyJoinColumn(name = "user_id")
public class Attendee extends User {

    private String profession;
    private String university;

    @OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL)
    private List<EventAttendee> eventAttendees;

    public Attendee(String username, String email, String password, Role role, String profession, String university) {
        super(username, email, password, role);
        this.profession = profession;
        this.university = university;
    }
}
