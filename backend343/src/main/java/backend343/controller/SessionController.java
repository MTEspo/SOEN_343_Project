package backend343.controller;

import backend343.models.Session;
import backend343.models.User;
import backend343.models.Speaker;
import backend343.service.SessionService;
import backend343.service.TicketService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/session")
public class SessionController {

    @Autowired
    private SessionService sessionService;


    @Autowired
    private TicketService ticketService;

    @GetMapping("/all-sessions")
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @PostMapping("/create/{scheduleId}")
    public ResponseEntity<Session> createSession(@RequestBody Session session, @PathVariable("scheduleId") Long scheduleId) {
        return ResponseEntity.ok(sessionService.createSession(session, scheduleId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable("id") Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/event")
    public ResponseEntity<Long> getEventIdFromSession(@PathVariable("id") Long id) {
        Long eventId = sessionService.getEventIdFromSession(id);
        return ResponseEntity.ok(eventId);
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<Long> getScheduleIdFromSession(@PathVariable Long id) {
        Long scheduleId = sessionService.getScheduleIdFromSession(id);
        return ResponseEntity.ok(scheduleId);
    }

    @GetMapping("/access/{userId}/{sessionId}")
    public boolean hasAccessToEvent(@PathVariable("userId") Long userId,@PathVariable("sessionId") Long sessionId) {
        return sessionService.hasAccessToEvent(userId, sessionId);
    }

    @GetMapping("/valid-sessions/{eventId}") // sessions that have speaker and have not passed
    public ResponseEntity<List<Session>> getValidSessionsForEvent(@PathVariable Long eventId) {
        List<Session> validSessions = sessionService.getValidSessionsForEvent(eventId);
        return ResponseEntity.ok(validSessions);
    }

    @GetMapping("/valid-sessions-for-speaker/{speakerId}") // sessions speakers could register for
    public ResponseEntity<List<Session>> getValidSessionsForSpeaker(@PathVariable Long speakerId) {
        List<Session> validSessions = sessionService.getValidSessionsForSpeaker(speakerId);
        return ResponseEntity.ok(validSessions);
    }

    @GetMapping("/{sessionId}/eligible-speakers") // list of speakers that are available for a session
    public ResponseEntity<List<Speaker>> getEligibleSpeakersForSession(@PathVariable Long sessionId) {
        List<Speaker> eligibleSpeakers = sessionService.getEligibleSpeakersForSession(sessionId);
        return ResponseEntity.ok(eligibleSpeakers);
    }

}
