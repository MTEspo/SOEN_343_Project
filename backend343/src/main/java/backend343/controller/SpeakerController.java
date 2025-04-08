package backend343.controller;

import backend343.dto.UpdateSpeakerOfferRequest;
import backend343.models.Organizer;
import backend343.models.Session;
import backend343.models.Speaker;
import backend343.models.SpeakerOffer;
import backend343.service.SpeakerOfferService;
import backend343.service.SpeakerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/speakers")
public class SpeakerController {

    private final SpeakerService speakerService;
    private final SpeakerOfferService speakerOfferService;

    @GetMapping("/all-speakers")
    public List<Speaker> getAllSpeakers() {
        return speakerService.getAllSpeakers();
    }

    @GetMapping("/get-speaker-by-id/{id}")
    public ResponseEntity<Speaker> getSpeakerById(@PathVariable Long id) {
        Speaker speaker = speakerService.findById(id);
        return ResponseEntity.ok(speaker);
    }

    @GetMapping("/get-speaker-by-email/{email}")
    public ResponseEntity<Speaker> getSpeakerByEmail(@PathVariable String email) {
        Speaker speaker = speakerService.findByEmail(email);
        return ResponseEntity.ok(speaker);
    }

    @GetMapping("/get-speaker-username/{speakerId}")
    public ResponseEntity<String> getSpeakerUsernameById(@PathVariable Long speakerId) {
        String username = speakerService.getUsernameById(speakerId);
        return ResponseEntity.ok(username);
    }

    @GetMapping("/get-speaker-average-rating/{speakerId}")
    public ResponseEntity<Double> getSpeakerAverageRating(@PathVariable Long speakerId) {
        Double averageRating = speakerService.getAverageRating(speakerId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/sessions/{speakerId}")
    public ResponseEntity<List<Session>> getSessionsBySpeakerId(@PathVariable Long speakerId) {
        List<Session> sessions = speakerService.getSessionsBySpeakerId(speakerId);
        return ResponseEntity.ok(sessions);
    }

    @PostMapping("/update-speaker-offer")
    public ResponseEntity<Void> updateSpeakerOffer(@RequestBody UpdateSpeakerOfferRequest request) {
        speakerOfferService.updateSpeakerOffer(request.getOfferId(), request.getStatus());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all-offers/{speakerId}")
    public List<SpeakerOffer> getAllOffers(@PathVariable Long speakerId) {
        return speakerService.getAllOffers(speakerId);
    }

    @GetMapping("/{speakerOfferId}/get-organizer")
    public ResponseEntity<Organizer> getOrganizer(@PathVariable Long speakerOfferId) {
        return ResponseEntity.ok(speakerOfferService.getOrganizer(speakerOfferId));
    }

    @GetMapping("/{speakerOfferId}/get-session")
    public ResponseEntity<Session> getSession(@PathVariable Long speakerOfferId) {
        return ResponseEntity.ok(speakerOfferService.getSession(speakerOfferId));
    }
}
