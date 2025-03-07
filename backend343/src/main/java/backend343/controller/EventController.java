package backend343.controller;

import backend343.models.Event;
import backend343.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/access/{userId}/{eventId}")
    public boolean hasAccessToEvent(@PathVariable Long userId,@PathVariable Long eventId) {
        return eventService.hasAccessToEvent(userId, eventId);
    }

    @PostMapping("/create")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(eventService.createEvent(event));
    }
}
