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

@Service
public class EventService {

    @Autowired
    private EventProxy eventProxy;

    public Event findById(Long id) {
        return eventProxy.getEventById(id);
    }

    public void save(Event event) {
        eventProxy.createEvent(event);
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
