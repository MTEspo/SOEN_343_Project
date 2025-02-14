package backend343.dto;

import lombok.Data;

@Data
public class VerifyUserDto {
    private String email;
    private String verificationCode;

    public String getEmail() {
        return email;
    }
    public String getVerificationCode() {
        return verificationCode;
    }
}
