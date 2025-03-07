package backend343.factory;

import backend343.dto.RegisterDto;
import backend343.enums.Role;
import backend343.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserFactoryProvider {
    private final Map<Role, UserFactory> factoryMap = new HashMap<>();

    @Autowired
    public UserFactoryProvider(List<UserFactory> factories) {
        for (UserFactory factory : factories) {
            factoryMap.put(factory.getRole(), factory);
        }
    }

    public UserFactory getFactory(Role role) {
        return factoryMap.get(role);
    }
}
