package backend343.controller;

import backend343.dto.VerifyTicketRequest;
import backend343.models.Session;
import backend343.models.Ticket;
import backend343.models.User;
import backend343.service.SessionService;
import backend343.service.TicketService;
import backend343.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private SessionService sessionService;

    @PostMapping("/check-in")
    public ResponseEntity<String> verifyTicket(@RequestBody VerifyTicketRequest request) {
        boolean ticketValid = ticketService.verifyTicket(request.getTicketCode());
        return ticketValid ? ResponseEntity.ok("Ticket is valid")
                : ResponseEntity.badRequest().body("Ticket is invalid");
    }

    @GetMapping("/all-users-tickets/{userId}")
    public ResponseEntity<List<Ticket>> getAllUserTickets(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(ticketService.getAllUserTickets(userId));
    }

    @PostMapping("/create/{userId}/{sessionId}")
    public ResponseEntity<Ticket> createTicket(@PathVariable("userId") Long userId,
            @PathVariable("sessionId") Long sessionId) {
        User user = userDetailsService.getUserById(userId);
        Session session = sessionService.getSessionById(sessionId);

        Ticket ticket = ticketService.createTicket(session, user, "test");
        return ResponseEntity.ok(ticket);
    }

    @GetMapping("/get-all-users-sessions/{userId}")
    public List<Session> getAllSessionsUserHasAccessTo(@PathVariable("userId") Long userId) {
        return ticketService.getAllUsersSessions(userId);
    }
}
