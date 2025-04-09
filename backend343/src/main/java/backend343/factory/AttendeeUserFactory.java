package backend343.factory;

import backend343.dto.RegisterDto;
import backend343.enums.Role;
import backend343.models.Attendee;
import backend343.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AttendeeUserFactory implements UserFactory{
    @Override
    public Role getRole() {
        return Role.ATTENDEE;
    }

    @Override
    public User createUser(RegisterDto input, PasswordEncoder passwordEncoder) {
        Attendee attendee = new Attendee(input.getUsername(), input.getEmail(), passwordEncoder.encode(input.getPassword()), input.getRole(), input.getProfession(), input.getUniversity());
        attendee.setInterests(input.getInterests());
        return attendee;
    }
}
