package backend343.service;

import backend343.models.Event;
import backend343.repository.EventRepository;
import backend343.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event findById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public void save(Event event) {
        eventRepository.save(event);
    }

    public Event createEvent(Event event) {
        // create stripe product for every event setting the stripe id same as its databse id

        //Event savedEvent = eventRepository.save(event);
        //
        //    // Create a Stripe product for the event
        //    ProductCreateParams params = ProductCreateParams.builder()
        //            .setName(event.getName())
        //            .setDescription(event.getDescription())
        //            .setActive(true)
        //            .build();
        //    Product product = Product.create(params);
        //
        //    // Save the Stripe product ID to the event record
        //    savedEvent.setStripeProductId(product.getId());
        //    eventRepository.save(savedEvent);
        //
        //    return savedEvent;
        return eventRepository.save(event)
                ;
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow();
    }
}
