package backend343.factory;

import backend343.dto.RegisterDto;
import backend343.enums.Role;
import backend343.enums.StakeholderType;
import backend343.models.Stakeholder;
import backend343.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StakeholderUserFactory implements UserFactory {
    
    @Override
    public Role getRole() {
        return Role.STAKEHOLDER;
    }

    @Override
    public User createUser(RegisterDto input, PasswordEncoder passwordEncoder) {
        return new Stakeholder(
                input.getUsername(),
                input.getEmail(),
                passwordEncoder.encode(input.getPassword()),
                input.getRole(),
                input.getStakeholderType(),
                input.getCompanyName()
        );
    }
}
