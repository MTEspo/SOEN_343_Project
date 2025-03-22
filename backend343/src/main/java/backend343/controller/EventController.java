package backend343.controller;

import backend343.models.Event;
import backend343.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/all-events")
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }

    @PostMapping("/update/description/{id}")
    public ResponseEntity<Event> updateDescription(@PathVariable Long id, @RequestBody String description) {
        return ResponseEntity.ok(eventService.updateEventDescription(id, description));
    }

    @PostMapping("/update/price/{id}")
    public ResponseEntity<Event> updatePrice(@PathVariable Long id, @RequestBody BigDecimal price) {
        return ResponseEntity.ok(eventService.updateEventPrice(id, price));
    }

    @PostMapping("/update/name/{id}")
    public ResponseEntity<Event> updateName(@PathVariable Long id, @RequestBody String name) {
        return ResponseEntity.ok(eventService.updateEventName(id, name));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }


}
