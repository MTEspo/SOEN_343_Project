package backend343.controller;

import backend343.dto.PromotionEmailRequest;
import backend343.service.OrganizerService;
import com.stripe.exception.StripeException;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizer")
public class OrganizerController {

    @Autowired
    private OrganizerService organizerService;
    @PostMapping("/email-promote")
    public void emailPromote(@RequestBody PromotionEmailRequest promotionEmailRequest) throws MessagingException, StripeException {
        // should include their email and name in the subject and or text
        organizerService.sendEmail(promotionEmailRequest);
    }
}
