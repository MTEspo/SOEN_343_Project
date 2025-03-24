package backend343.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import backend343.chatRoom.ChatRoom;
import backend343.chatRoom.ChatroomService;
import backend343.models.Schedule;
import backend343.models.Speaker;
import backend343.repository.SpeakerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend343.models.Session;
import backend343.repository.SessionRepository;

@Service
public class SessionService {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private SpeakerRepository speakerRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session createSession(Session session, Long scheduleId) {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        session.setSchedule(schedule);

        ChatRoom chatRoom = new ChatRoom();
        session.setChatroom(chatRoom);

        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public Long getEventIdFromSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
        .map(session -> session.getSchedule().getEvent().getId())
        .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public Long getScheduleIdFromSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
        .map(session -> session.getSchedule().getId())
        .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public boolean hasAccessToEvent(Long userId, Long sessionId) {
        return ticketService.hasEventAccess(userId, sessionId);
    }

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public List<Session> getValidSessionsForSpeaker(Long speakerId) {
        List<Session> validSessions = new ArrayList<>();
        List<Session> allSessions = sessionRepository.findAll();

        for (Session session : allSessions) {
            if (session.getSpeaker() == null && !hasSessionPassed(session, session.getSchedule())) {
                validSessions.add(session);
            }
        }

        return validSessions;
    }

    public List<Session> getValidSessionsForEvent(Long eventId) {
        List<Session> validSessions = new ArrayList<>();
        List<Schedule> schedules = scheduleService.getSchedulesForEvent(eventId);

        for (Schedule schedule : schedules) {
            for (Session session : schedule.getSessions()) {
                if (session.getSpeaker() != null && !hasSessionPassed(session, schedule)) {
                    validSessions.add(session);
                }
            }
        }

        return validSessions;
    }

    private boolean hasSessionPassed(Session session, Schedule schedule) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (schedule.getDate().isBefore(currentDate)) {
            return true;
        } else if (schedule.getDate().equals(currentDate)) {
            return session.getStartTime().isBefore(currentTime);
        }

        return false;
    }

    public List<Speaker> getEligibleSpeakersForSession(Long sessionId) {
        Session session = getSessionById(sessionId);
        Schedule schedule = session.getSchedule();

        List<Speaker> allSpeakers = speakerRepository.findAll();
        List<Speaker> eligibleSpeakers = new ArrayList<>();

        for (Speaker speaker : allSpeakers) {
            if (isSpeakerEligibleForSession(speaker, session, schedule)) {
                eligibleSpeakers.add(speaker);
            }
        }

        return eligibleSpeakers;
    }

    private boolean isSpeakerEligibleForSession(Speaker speaker, Session session, Schedule schedule) {
        if (hasSessionPassed(session, schedule)) {
            return false;
        }

        List<Session> speakerSessions = speaker.getSessions();

        for (Session speakerSession : speakerSessions) {
            if (speakerSession.getSchedule().getDate().equals(schedule.getDate())
                    && isTimeOverlapping(speakerSession.getStartTime(), speakerSession.getEndTime(), session.getStartTime(), session.getEndTime())) {
                return false;
            }
        }

        return true;
    }

    private boolean isTimeOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return (start1.isBefore(end2) && start2.isBefore(end1));
    }
}
