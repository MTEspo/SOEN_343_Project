package backend343.controller;

import backend343.dto.CreateSpeakerOfferRequest;
import backend343.dto.PromotionEmailRequest;
import backend343.service.OrganizerService;
import backend343.service.SpeakerOfferService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizer")
public class OrganizerController {

    @Autowired
    private OrganizerService organizerService;
    @Autowired
    private SpeakerOfferService speakerOfferService;
    @PostMapping("/email-promote")
    public void emailPromote(@RequestBody PromotionEmailRequest promotionEmailRequest) throws MessagingException {
        // should include their email and name in the subject and or text
        organizerService.sendEmail( promotionEmailRequest.getSubject(), promotionEmailRequest.getText());
    }

    @PostMapping("/create-speaker-offer")
    public ResponseEntity<Void> createSpeakerOffer(@RequestBody CreateSpeakerOfferRequest request) {
        speakerOfferService.createSpeakerOffer(request.getSessionId(), request.getSpeakerId(),request.getOrganizerId());
        return ResponseEntity.ok().build();
    }
}
