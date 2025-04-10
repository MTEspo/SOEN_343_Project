package backend343.models;

import backend343.enums.Role;
import backend343.chatRoom.ChatObserver;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@Table(name = "users") //rename table to avoid conflicts with user keyword
@Inheritance(strategy = InheritanceType.JOINED) //each subclass uses the id from user
public class User implements UserDetails, ChatObserver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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

    @Enumerated(EnumType.STRING) //stores enum as a string in database
    private Role role;

    @ElementCollection //storing map in database
    @CollectionTable(name = "user_chat_notifications", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "chatroom_id")
    @Column(name = "notification_count")
    private Map<Long, Integer> chatroomNotifications = new HashMap<>();

    public User(){}
    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }


    // Getter and Setter for id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for username
    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for password
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for enabled
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // Getter and Setter for verificationCode
    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    // Getter and Setter for verificationCodeExpiresAt
    public LocalDateTime getVerificationCodeExpiresAt() {
        return verificationCodeExpiresAt;
    }

    public void setVerificationCodeExpiresAt(LocalDateTime verificationCodeExpiresAt) {
        this.verificationCodeExpiresAt = verificationCodeExpiresAt;
    }

    // Getter and Setter for role
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Additional methods from UserDetails interface

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
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
    public void update(Long chatroomId, Long senderId) {
        System.out.println("New message in the chatRoom");
        incrementChatroomNotifications(chatroomId);
    }

    //increment notifications for a specific chatroom
    public void incrementChatroomNotifications(Long chatroomId) {
        chatroomNotifications.put(chatroomId, chatroomNotifications.getOrDefault(chatroomId, 0) + 1);
    }

    //reset notifs when they join
    public void resetChatroomNotifications(Long chatroomId) {
        chatroomNotifications.put(chatroomId, 0);
    }

    public int getTotalNotifications() {
        return chatroomNotifications.values().stream().mapToInt(Integer::intValue).sum();
    }
}
