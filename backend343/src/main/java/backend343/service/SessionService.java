package backend343.service;

import java.util.List;
import java.util.Optional;

import backend343.models.Event;
import backend343.models.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend343.models.Session;
import backend343.repository.SessionRepository;

@Service
public class SessionService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ScheduleService scheduleService;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session createSession(Session session, Long scheduleId) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        session.setSchedule(schedule);
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
