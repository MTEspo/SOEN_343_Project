package backend343.controller;

import backend343.models.Session;
import backend343.service.SessionService;

import java.util.List;

import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @PostMapping("/create/{scheduleId}")
    public ResponseEntity<Session> createSession(@RequestBody Session session, @PathVariable Long scheduleId) throws StripeException {
        return ResponseEntity.ok(sessionService.createSession(session, scheduleId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    //endpoint to get eventId from a session
    @GetMapping("/{id}/event")
    public ResponseEntity<Long> getEventIdFromSession(@PathVariable Long id) {
        Long eventId = sessionService.getEventIdFromSession(id);
        return ResponseEntity.ok(eventId);
    }

    @GetMapping("/access/{userId}/{sessionId}")
    public boolean hasAccessToEvent(@PathVariable Long userId,@PathVariable Long sessionId) {
        return sessionService.hasAccessToEvent(userId, sessionId);
    }

}
