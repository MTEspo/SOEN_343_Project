package backend343.models;

import backend343.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "attendees")
@PrimaryKeyJoinColumn(name = "user_id")
public class Attendee extends User {

    private String profession;
    private String university;

    public Attendee() {
        super();
    }

    public Attendee(String username, String email, String password, Role role, String profession, String university) {
        super(username, email, password, role);
        this.profession = profession;
        this.university = university;
    }
}
