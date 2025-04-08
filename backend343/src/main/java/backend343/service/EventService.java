package backend343.service;

import backend343.enums.EventType;
import backend343.enums.TicketStatus;
import backend343.models.*;
import backend343.proxy.EventProxy;
import backend343.repository.EventRepository;
import backend343.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventProxy eventProxy;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketService ticketService;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event findById(Long id) {
        return eventProxy.getEventById(id);
    }
  
    public Event getEventDirectlyFromRepo(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with ID: " + id));
    }
    
    public void save(Event event) {
        eventProxy.save(event);
    }

    public Event createEvent(Event event,Long organizer_id) {
        return eventProxy.createEvent(event,organizer_id);
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

    public Analytics getAnalytics(Long id) {
        Event event = getEventDirectlyFromRepo(id);
        List<Schedule> schedules = event.getSchedules();
        Set<Speaker> speakers = new HashSet<>();
        Set<User> users = new HashSet<>();
        List<Ticket> tickets = new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO;
        for(Schedule schedule : schedules) {
            tickets.addAll(ticketService.getTicketsBySessionId(schedule.getId()));
        }

        List<Ticket> activeTickets = tickets.stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.ACTIVE)
                .toList();

        for (Ticket ticket : activeTickets) {
            sum = sum.add(ticket.getAmountPaid());
            speakers.add(ticket.getSession().getSpeaker());
            users.add(ticket.getUser());
        }

        return Analytics.builder()
                .speakers(new ArrayList<>(speakers))
                .amountGenerated(sum)
                .attendees(new ArrayList<>(users))
                .build();
    }

}
