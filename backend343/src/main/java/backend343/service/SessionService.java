package backend343.service;

import java.util.List;

import backend343.chatRoom.ChatRoom;
import backend343.chatRoom.ChatroomService;
import backend343.models.Schedule;
import backend343.stripe.StripeProductService;
import com.stripe.exception.StripeException;
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
    private ChatroomService chatRoomService;
    @Autowired
    private StripeProductService stripeProductService;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session createSession(Session session, Long scheduleId) throws StripeException {
        Schedule schedule = scheduleService.getScheduleById(scheduleId);
        session.setSchedule(schedule);
        ChatRoom chatRoom = new ChatRoom();
        session.setChatroom(chatRoom);
        String stripeProductId = stripeProductService.createStripeProduct(session);
        session.setStripeProductId(stripeProductId);
        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));
    }

    //method to get the eventID from the session
    public Long getEventIdFromSession(Long sessionId) {
        return sessionRepository.findById(sessionId)
        .map(session -> session.getSchedule().getEvent().getId())
        .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public boolean hasAccessToEvent(Long userId, Long sessionId) {
        return ticketService.hasEventAccess(userId, sessionId);
    }

    public Session findByStripeProductId(String stripeProductId) {
        return sessionRepository.findByStripeProductId(stripeProductId);
    }
}
