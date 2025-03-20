package backend343.service;

import backend343.enums.EventType;
import backend343.models.Event;
import backend343.proxy.EventProxy;
import backend343.repository.EventRepository;
import backend343.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventProxy eventProxy;

    @Autowired
    private EventRepository eventRepository;

    public List<Event> findAll(){
            return eventRepository.findAll();
        }

    public Event findById(Long id) {
        return eventProxy.getEventById(id);
    }

    public void save(Event event) {
        eventProxy.createEvent(event);
    }

    public Event createEvent(Event event) {
        return eventProxy.createEvent(event);
    }
    public Event updateEventDescription(Long id,String description) {
        return eventProxy.updateEventDescription(id, description);
    }

    public Event updateEventPrice(Long id, BigDecimal price) {
        return eventProxy.updateEventPrice(id, price);
    }

    public Event updateEventName(Long id, String name) {
        return eventProxy.updateEventName(id, name);
    }

}
