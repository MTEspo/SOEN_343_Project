package backend343.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend343.models.Session;
import backend343.repository.SessionRepository;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    public Optional<Session> getSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    //method to get the eventID from the session
    public Long getEventIdFromSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
        .map(session -> session.getSchedule().getEvent().getId())
        .orElseThrow(() -> new RuntimeException("Session not found"));
    }
}
