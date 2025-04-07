package backend343.models;

import backend343.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@Builder
@Table(name = "organizers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Organizer extends User {

    private String organization;

    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Event> events = new ArrayList<>();

    public Organizer() {
    }

    public Organizer(String username, String email, String password, Role role, String organization) {
        super(username, email, password, role);
        this.organization = organization;
    }
}