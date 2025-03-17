package backend343.proxy;

import backend343.logger.LoggerSingleton;
import backend343.models.Event;
import backend343.repository.EventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventProxy {
    
    private EventRepository eventRepository;
    private static final Map<Long, Event> eventCache = new ConcurrentHashMap<>(); // Caching Layer
    private static final LoggerSingleton logger = LoggerSingleton.getInstance(); 

    @Autowired
    public EventProxy(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event getEventById(Long id) {
        if (eventCache.containsKey(id)) {
            logger.logInfo("[CACHE HIT] - Event ID " + id + " fetched from cache.");
            return eventCache.get(id);
        }

        logger.logInfo("[CACHE MISS] - Fetching Event ID " + id + " from database.");
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        eventCache.put(id, event); //Cache the Event
        return event;
    }

    public Event createEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        eventCache.put(savedEvent.getId(), savedEvent); //Cache New Event
        logger.logInfo("[CACHE ADD] - Event ID " + savedEvent.getId() + " added to cache.");
        return savedEvent;
    }

    public Event updateEventDescription(Long id, String description) {
        Event event = getEventById(id);
        event.setDescription(description);
        Event updatedEvent = eventRepository.save(event);
        invalidateCache(id);
        return updatedEvent;
    }

    public Event updateEventPrice(Long id, BigDecimal price) {
        Event event = getEventById(id);
        event.setPrice(price);
        Event updatedEvent = eventRepository.save(event);
        invalidateCache(id);
        return updatedEvent;
    }

    public Event updateEventName(Long id, String name) {
        Event event = getEventById(id);
        event.setName(name);
        Event updatedEvent = eventRepository.save(event);
        invalidateCache(id);
        return updatedEvent;
    }

    //clearing the cache method
    private void invalidateCache(Long id) {
        if (eventCache.containsKey(id)) {
            eventCache.remove(id);
            logger.logInfo("[CACHE INVALIDATION] - Event ID " + id + " removed from cache due to update.");
        }
    }
}
