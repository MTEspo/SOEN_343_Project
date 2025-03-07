package backend343.factory;

import backend343.dto.RegisterDto;
import backend343.enums.Role;
import backend343.models.Organizer;
import backend343.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class OrganizerUserFactory implements UserFactory {
    @Override
    public Role getRole() {
        return Role.ORGANIZER;
    }

    @Override
    public User createUser(RegisterDto input, PasswordEncoder passwordEncoder) {
        return new Organizer(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()), input.getRole(),input.getOrganization());
    }
}
