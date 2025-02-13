package backend343.dto;

import backend343.enums.Role;
import lombok.Data;

@Data
public class RegisterDto {
    private String email;
    private String password;
    private String username;
    private Role role;
    private String expertise; // For Speaker
    private String organization; // For Organizer
    private String profession; // For Attendee
    private String university; // For Attendee
}
