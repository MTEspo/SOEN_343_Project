package backend343.controller;

import backend343.dto.CreateEventRating;
import backend343.models.EventRating;
import backend343.service.EventRatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event-ratings")
@RequiredArgsConstructor
public class EventRatingController {

    private final EventRatingService eventRatingService;

    @PostMapping("/create")
    public ResponseEntity<EventRating> createRating(@RequestBody CreateEventRating request) {
        EventRating eventRating = eventRatingService.createRating(request.getEventId(), request.getUserId(), request.getRating());
        return ResponseEntity.ok(eventRating);
    }

}
