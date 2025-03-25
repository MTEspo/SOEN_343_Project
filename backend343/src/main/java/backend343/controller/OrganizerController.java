package backend343.controller;

import backend343.dto.CreateSpeakerOfferRequest;
import backend343.dto.PromotionEmailRequest;
import backend343.models.Event;
import backend343.responses.SpeakerOfferResponse;
import backend343.service.OrganizerService;
import backend343.service.SpeakerOfferService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<SpeakerOfferResponse> createSpeakerOffer(@RequestBody CreateSpeakerOfferRequest request) {
        SpeakerOfferResponse response = speakerOfferService.createSpeakerOffer(request.getSessionId(), request.getSpeakerId(),request.getOrganizerId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/events")
    public List<Event> getOrganizerEvents(@PathVariable Long id) {
        return organizerService.findOrganizerById(id).getEvents();
    }
}
