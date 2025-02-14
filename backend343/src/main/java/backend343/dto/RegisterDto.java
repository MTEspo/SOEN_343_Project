package backend343.dto;

import backend343.enums.Role;
import lombok.Data;

public class RegisterDto {
    private String email;
    private String password;
    private String username;
    private Role role;
    private String expertise; // For Speaker
    private String organization; // For Organizer
    private String profession; // For Attendee
    private String university; // For Attendee

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public String getExpertise() {
        return expertise;
    }

    public String getOrganization() {
        return organization;
    }

    public String getProfession() {
        return profession;
    }

    public String getUniversity() {
        return university;
    }
}

