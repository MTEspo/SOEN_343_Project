package backend343.service;

import backend343.logger.LoggerSingleton;
import backend343.models.Ticket;
import backend343.models.User;
import backend343.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static backend343.authentication.AuthenticationService.generateVerificationCode;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();


    public boolean verifyTicket(String ticketCode) {
        logger.logInfo("Verifying ticket code " + ticketCode);
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode);

        if (ticket == null) {
            logger.logInfo("Ticket code " + ticketCode + " not found");
            return false;
        }

        if (ticket.getIsCodeUsed()) {
            logger.logInfo("Ticket code " + ticketCode + " already used");
            return false;
        }

        ticket.setIsCodeUsed(true);
        ticketRepository.save(ticket);
        logger.logInfo("Ticket code " + ticketCode + " verified successfully");
        return true;
    }

    public Ticket createTicket(backend343.models.Session session, User user) {
        logger.logInfo("Creating new ticket for user " + user.getId() + " and session " + session.getId());
        String ticketCode;
        do {
            ticketCode = generateVerificationCode();
        } while (ticketRepository.findByTicketCode(ticketCode) != null);

        Ticket ticket = Ticket.builder()
                .user(user)
                .session(session)
                .registrationDate(LocalDateTime.now())
                .ticketCode(ticketCode)
                .isCodeUsed(false)
                .build();
        ticket = ticketRepository.save(ticket);
        logger.logInfo("New ticket created successfully for user " + user.getId() + " and session " + session.getId());
        return ticket;
    }

    public boolean hasEventAccess(Long userId, Long sessionId) {
        logger.logInfo("Checking event access for user " + userId + " and session " + sessionId);
        boolean hasAccess = ticketRepository.hasEventAccess(userId, sessionId);
        logger.logInfo("Event access result for user " + userId + " and session " + sessionId + ": " + hasAccess);
        return hasAccess;
    }

    public List<User> getUsersWithAccessToSession(Long sessionId) {
        // Find all tickets related to the given session ID
        List<Ticket> tickets = ticketRepository.findAllBySessionId(sessionId);
        
        //get associated users from each ticket
        return tickets.stream()
                      .map(Ticket::getUser)
                      .collect(Collectors.toList());
    }
}
