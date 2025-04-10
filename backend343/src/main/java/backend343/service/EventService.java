package backend343.service;

import backend343.enums.EventType;
import backend343.enums.Tag;
import backend343.enums.TicketStatus;
import backend343.models.*;
import backend343.proxy.EventProxy;
import backend343.repository.EventInvestmentRepository;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.*;

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

    @Autowired
    private EventInvestmentRepository eventInvestmentRepository;

    @Autowired
    private AttendeeService attendeeService;


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
        BigDecimal ticketRevenue = BigDecimal.ZERO;
    
        for (Schedule schedule : schedules) {
            tickets.addAll(ticketService.getTicketsBySessionId(schedule.getId()));
        }
    
        List<Ticket> activeTickets = tickets.stream()
                .filter(ticket -> ticket.getStatus() == TicketStatus.ACTIVE)
                .toList();
    
        for (Ticket ticket : activeTickets) {
            ticketRevenue = ticketRevenue.add(ticket.getAmountPaid());
            speakers.add(ticket.getSession().getSpeaker());
            users.add(ticket.getUser());
        }
    
        BigDecimal stakeholderContributions = event.getInvestments().stream()
        .map(EventInvestment::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        List<Stakeholder> stakeholders = event.getStakeholders();
    
        return Analytics.builder()
                .speakers(new ArrayList<>(speakers))
                .attendees(new ArrayList<>(users))
                .amountGenerated(ticketRevenue)
                .stakeholderContributions(stakeholderContributions)
                .totalRevenue(ticketRevenue.add(stakeholderContributions))
                .fundingGoal(event.getFundingGoal())
                .stakeholders(stakeholders)
                .averageRating(event.getAverageRating())
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
    
        EventInvestment investment = EventInvestment.builder()
            .event(event)
            .stakeholder(stakeholder)
            .amount(amount)
            .build();
    
        event.getInvestments().add(investment);
        //make sure that stakeholder isnt added to the list again, if they wanna invest for a second time or smtg
        if (!event.getStakeholders().contains(stakeholder)) {
            event.getStakeholders().add(stakeholder);
        }
    
        eventRepository.save(event);
        return true;
    }

    public List<Map<String, Object>> getInvestmentsByStakeholder(Long stakeholderId) {
        List<EventInvestment> investments = eventInvestmentRepository.findByStakeholderId(stakeholderId);
    
        List<Map<String, Object>> result = new ArrayList<>();
        for (EventInvestment investment : investments) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("eventId", investment.getEvent().getId());
            entry.put("eventName", investment.getEvent().getName());
            entry.put("amountInvested", investment.getAmount());
            entry.put("fundingGoal", investment.getEvent().getFundingGoal());
            result.add(entry);
        }
        return result;
    }

    public List<Event> getRecommendedEvents(Long userId) {
        Attendee attendee = attendeeService.getAttendeeById(userId);
        List<Tag> interests = attendee.getInterests();
        if (interests.isEmpty()) {
            return Collections.emptyList();
        }
        return eventRepository.findEventsByTags(interests);
    }

    public Event updateEventTags(Long id, List<Tag> tags) {
        Event event = getEventDirectlyFromRepo(id);
        event.setTags(tags);
        return eventRepository.save(event);
    }

}
