package backend343.factory;

import backend343.dto.RegisterDto;
import backend343.enums.Role;
import backend343.models.Speaker;
import backend343.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpeakerUserFactory implements UserFactory{
    @Override
    public Role getRole() {
        return Role.SPEAKER;
    }

    @Override
    public User createUser(RegisterDto input, PasswordEncoder passwordEncoder) {
        return new Speaker(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()), input.getRole(),input.getExpertise());
    }
}
