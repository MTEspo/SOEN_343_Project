package backend343.service;

import backend343.models.Ticket;
import backend343.models.User;
import backend343.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static backend343.authentication.AuthenticationService.generateVerificationCode;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public boolean verifyTicket(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode);

        if (ticket == null) {
            return false;
        }

        if (ticket.getIsCodeUsed()) {
            return false;
        }

        ticket.setIsCodeUsed(true);
        ticketRepository.save(ticket);
        return true;
    }

    public Ticket createTicket(backend343.models.Session session, User user) {
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
        return ticketRepository.save(ticket);
    }

    public boolean hasEventAccess(Long userId, Long sessionId) {
        return ticketRepository.hasEventAccess(userId, sessionId);
    }
}
