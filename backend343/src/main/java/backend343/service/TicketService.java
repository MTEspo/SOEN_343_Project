package backend343.service;

import backend343.enums.TicketStatus;
import backend343.logger.LoggerSingleton;
import backend343.models.Ticket;
import backend343.models.User;
import backend343.repository.TicketRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        if (ticket.getStatus() == TicketStatus.REFUNDED) {
            logger.logInfo("Ticket code " + ticketCode + " has been refunded");
            return false;
        }

        ticket.setIsCodeUsed(true);
        ticketRepository.save(ticket);
        logger.logInfo("Ticket code " + ticketCode + " verified successfully");
        return true;
    }

    public Ticket createTicket(backend343.models.Session session, User user,String stripePaymentId) {
        logger.logInfo("Creating new ticket for user " + user.getId() + " and session " + session.getId());
        String ticketCode;
        do {
            ticketCode = generateVerificationCode();
        } while (ticketRepository.findByTicketCode(ticketCode) != null);

        Ticket ticket = Ticket.builder()
                .user(user)
                .session(session)
                .stripePaymentId(stripePaymentId)
                .registrationDate(LocalDateTime.now())
                .ticketCode(ticketCode)
                .isCodeUsed(false)
                .status(TicketStatus.ACTIVE)
                .build();
        ticket = ticketRepository.save(ticket);
        logger.logInfo("New ticket created successfully for user " + user.getId() + " and session " + session.getId());
        return ticket;
    }

    public void refundTicket(Ticket ticket) {
        ticket.setStatus(TicketStatus.REFUNDED);
        ticketRepository.save(ticket);
    }

    public void deleteTicket(Ticket ticket) {
        logger.logInfo("Deleting ticket by id: " + ticket.getId());
        ticketRepository.deleteById(ticket.getId());
    }

    public boolean hasEventAccess(Long userId, Long sessionId) {
        logger.logInfo("Checking event access for user " + userId + " and session " + sessionId);
        boolean hasAccess = ticketRepository.hasEventAccessAndActiveStatus(userId, sessionId);
        logger.logInfo("Event access result for user " + userId + " and session " + sessionId + ": " + hasAccess);
        return hasAccess;
    }

    public Ticket getTicketBySessionIdAndUserId(Long sessionId, Long userId) {
        return ticketRepository.findBySessionIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Ticket not found"));
    }
}
