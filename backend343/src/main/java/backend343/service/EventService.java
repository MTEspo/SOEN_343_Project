package backend343.service;

import backend343.enums.EventType;
import backend343.enums.TicketStatus;
import backend343.models.*;
import backend343.proxy.EventProxy;
import backend343.repository.EventRepository;
import backend343.repository.ResourceRepository;
import backend343.repository.TicketRepository;

import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private ResourceRepository resourceRepository;

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

    public Event addFilesToEvent(Long id, MultipartFile[] files) {
        try {
            Event event = findById(id);
            for (MultipartFile file : files) {
                Resource resource = Resource.builder()
                        .name(file.getOriginalFilename())
                        .type(file.getContentType())
                        .content(file.getBytes())
                        .event(event)
                        .build();
                resourceRepository.save(resource);
            }
            return event;
        } catch (IOException e) {
            throw new RuntimeException("Error adding files to event", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while adding files to event", e);
        }
    }

    public List<Resource> getSavedResources(Long id) {
        Event event = getEventDirectlyFromRepo(id);
        return event.getResources();
    }

    public Event removeFileFromEvent(Long id, Long fileId) {
        Event event = getEventDirectlyFromRepo(id);
        Resource resourceToRemove = event.getResources().stream()
                .filter(resource -> resource.getId().equals(fileId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + fileId));
        event.getResources().remove(resourceToRemove);
        resourceRepository.deleteById(fileId);
        return eventRepository.save(event);
    }

    public Resource getResource(Long id, Long resourceId) {
        Event event = getEventDirectlyFromRepo(id);
        return event.getResources().stream()
                .filter(resource -> resource.getId().equals(resourceId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + resourceId));
    }

    public boolean handleStakeholderInvestment(Stakeholder stakeholder, Long eventId, BigDecimal amount) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty() || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
    
        Event event = optionalEvent.get();

        if (!event.getStakeholders().contains(stakeholder)) {
            event.getStakeholders().add(stakeholder);
        }
    
        // Update or add investment amount
        Map<Stakeholder, BigDecimal> investments = event.getInvestments();
        BigDecimal existingAmount = investments.getOrDefault(stakeholder, BigDecimal.ZERO);
        investments.put(stakeholder, existingAmount.add(amount));
    
        eventRepository.save(event);
        return true;
    }

}
