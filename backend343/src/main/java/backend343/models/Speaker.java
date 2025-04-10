package backend343.models;

import backend343.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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
    private Double averageRating;

    @ManyToMany
    @JoinTable(
            name = "speaker_sessions",
            joinColumns = @JoinColumn(name = "speaker_id"),
            inverseJoinColumns = @JoinColumn(name = "session_id")
    )
    private List<Session> sessions = new ArrayList<>();

    public Speaker() {
    }

    public Speaker(String username, String email, String password, Role role, String expertise) {
        super(username, email, password, role);
        this.expertise = expertise;
        this.averageRating = 0.0;
    }
}
