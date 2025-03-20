package backend343.controller;

import backend343.dto.VerifyTicketRequest;
import backend343.models.Ticket;
import backend343.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/check-in")
    public ResponseEntity<String> verifyTicket(@RequestBody VerifyTicketRequest request) {
            boolean ticketValid = ticketService.verifyTicket(request.getTicketCode());
            return ticketValid? ResponseEntity.ok("Ticket is valid") : ResponseEntity.badRequest().body("Ticket is invalid");
    }

    @GetMapping("/all-users-tickets/{userId}")
    public ResponseEntity<List<Ticket>> getAllUserTickets(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getAllUserTickets(userId));
    }
}
