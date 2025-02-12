package backend343.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String email;
    private String password;
    private String username;
}
