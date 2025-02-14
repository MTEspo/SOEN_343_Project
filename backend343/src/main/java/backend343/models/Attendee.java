package backend343.models;

import backend343.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


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

    @OneToMany(mappedBy = "attendee", cascade = CascadeType.ALL)
    private List<EventAttendee> eventAttendees = new ArrayList<>();

    public Attendee(String username, String email, String password, Role role, String profession, String university) {
        super(username, email, password, role);
        this.profession = profession;
        this.university = university;
    }
}
