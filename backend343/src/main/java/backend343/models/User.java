package backend343.models;

import backend343.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users") //rename table to avoid conflicts with user keyword
@Inheritance(strategy = InheritanceType.JOINED) //each subclass uses the id from user
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    private boolean enabled;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_expiration")
    private LocalDateTime verificationCodeExpiresAt;

    //not creating whole classes for diff types of users - they will be mapped to events based on role
    //your role only allows you to do certain methods, but not stop you from joining any event
    //ex. a speaker does not have to speak at an event.
    //they are only speaking at events that they are listed as EventSpeakers for
    //Eventspeaker = class dedicated to linking an event to users who WILL speak at event

    //in later stages can check for role permissions with spring security annotation
    //ex @PreAuthorize("hasRole('ADMIN')")
    @Enumerated(EnumType.STRING) //stores enum as a string in database
    private Role role;

    //an event can have many attendees, but each eventAttendee is associated to one event
    //but a user can still attend as many events they want
    //mapped by the event field in eventAttendee
    //if an event is deleted, all associated attendees in EventAttendee table will be deleted.

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled(){
        return enabled;
    }

}