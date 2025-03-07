package backend343.factory;

import backend343.dto.RegisterDto;
import backend343.enums.Role;
import backend343.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserFactory {
    Role getRole();
    User createUser(RegisterDto input, PasswordEncoder passwordEncoder);
}
