package backend343.models;

import backend343.enums.Role;
import backend343.enums.StakeholderType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@PrimaryKeyJoinColumn(name = "user_id")
public class Stakeholder extends User{

    private String companyName;
    private String description;

    @Enumerated(EnumType.STRING)
    private StakeholderType stakeholderType;

    @ManyToMany(mappedBy = "stakeholders")
    @JsonIgnore
    private List<Event> investedEvents = new ArrayList<>();

    public Stakeholder(String username, String email, String password, Role role, StakeholderType stakeholderType, String companyName) {
        super(username, email, password, role);
        this.stakeholderType = stakeholderType;
        this.companyName = companyName;
    }

    @OneToMany(mappedBy = "stakeholder", cascade = CascadeType.ALL)
    private List<EventInvestment> investments = new ArrayList<>();
}