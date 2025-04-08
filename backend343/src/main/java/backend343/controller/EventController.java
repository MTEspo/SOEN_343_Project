package backend343.controller;

import backend343.models.Analytics;
import backend343.models.Event;
import backend343.models.Resource;
import backend343.models.Schedule;
import backend343.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

    @PostMapping("/create/{organizer_id}")
    public ResponseEntity<Event> createEvent(@RequestBody Event event, @PathVariable("organizer_id") Long organizer_id) {
        return ResponseEntity.ok(eventService.createEvent(event,organizer_id));
    }

    @PostMapping("/update/description/{id}")
    public ResponseEntity<Event> updateDescription(@PathVariable("id") Long id, @RequestBody String description) {
        return ResponseEntity.ok(eventService.updateEventDescription(id, description));
    }

    @PostMapping("/update/price/{id}")
    public ResponseEntity<Event> updatePrice(@PathVariable("id") Long id, @RequestBody BigDecimal price) {
        return ResponseEntity.ok(eventService.updateEventPrice(id, price));
    }

    @PostMapping("/update/name/{id}")
    public ResponseEntity<Event> updateName(@PathVariable("id") Long id, @RequestBody String name) {
        return ResponseEntity.ok(eventService.updateEventName(id, name));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @GetMapping("/{id}/schedules")
    public List<Schedule> getSchedulesFromEvent(@PathVariable("id") Long id) {
        return eventService.getEventDirectlyFromRepo(id).getSchedules();
    }

    @GetMapping("/{id}/get-event-analytics")
    public ResponseEntity<Analytics> getEventAnalytics(@PathVariable("id") Long id) {
        return ResponseEntity.ok(eventService.getAnalytics(id));
    }

    @PostMapping("/{id}/add-files")
    public ResponseEntity<Event> addFileToEvent(@PathVariable("id") Long id, @RequestParam("files") MultipartFile[] files) {
        return ResponseEntity.ok(eventService.addFilesToEvent(id, files));
    }

    @PostMapping("/{id}/remove-file/{fileId}")
    public ResponseEntity<Event> removeFile(@PathVariable("id") Long id, @PathVariable("fileId") Long fileId) {
        return ResponseEntity.ok(eventService.removeFileFromEvent(id, fileId));
    }

    @GetMapping("/{id}/resources")
    public ResponseEntity<List<Resource>> getSavedResources(@PathVariable Long id) {
        List<Resource> resources = eventService.getSavedResources(id);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}/resources/{resourceId}/download")
    public ResponseEntity<byte[]> getResource(@PathVariable("id") Long id, @PathVariable("resourceId") Long resourceId) {
        Resource resource = eventService.getResource(id, resourceId);
        return ResponseEntity.ok()
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getName() + "\"")
                .contentType(MediaType.parseMediaType(resource.getType()))
                .body(resource.getContent());
    }


}
