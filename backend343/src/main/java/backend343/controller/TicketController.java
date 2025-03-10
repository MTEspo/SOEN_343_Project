package backend343.controller;

import backend343.dto.VerifyTicketRequest;
import backend343.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkin")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @PostMapping("/verify-ticket")
    public ResponseEntity<String> verifyTicket(@RequestBody VerifyTicketRequest request) {
            boolean ticketValid = ticketService.verifyTicket(request.getTicketCode());
            return ticketValid? ResponseEntity.ok("Ticket is valid") : ResponseEntity.badRequest().body("Ticket is invalid");
    }
}
