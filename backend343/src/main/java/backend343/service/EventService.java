package backend343.service;

import backend343.models.Event;
import backend343.repository.EventRepository;
import backend343.repository.UserEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private UserEventRepository userEventRepository;

    @Autowired
    private EventRepository eventRepository;

    public boolean hasAccessToEvent(Long userId, Long eventId) {
        return userEventRepository.hasEventAccess(userId, eventId);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }
}
