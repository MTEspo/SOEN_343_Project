package backend343.service;

import backend343.models.User;
import backend343.repository.OrganizerRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizerService {
    @Autowired
    private OrganizerRepository organizerRepostiory;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private EmailService emailService;

    public void sendEmail(String subject, String text) throws MessagingException {
        List<User> users = userDetailsService.getAllUsers();
        for (User user : users) {
            emailService.sendEmail(user.getEmail(), subject, text);
        }
    }

}
