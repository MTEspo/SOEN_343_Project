package backend343.service;

import backend343.logger.LoggerSingleton;
import backend343.models.Event;
import backend343.models.EventRating;
import backend343.models.User;
import backend343.repository.EventRatingRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventRatingService {

    private final EventRatingRepository eventRatingRepository;
    private final EventService eventService;
    private final UserDetailsServiceImpl userService;
    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

    // better implementation would check to see if user even has access to the event before creating the rating **
    @Transactional
    public EventRating createRating(Long eventId, Long userId, Integer rating) {
        try {
            Event event = eventService.findById(eventId);
            User user = userService.getUserById(userId);

            if (rating < 1 || rating > 5) {
                throw new Exception("Rating must be between 1 and 5");
            }

            if (hasUserRatedEvent(eventId, userId)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User has already rated this event");
            }

            EventRating eventRating = EventRating.builder()
                    .event(event)
                    .rater(user)
                    .rating(rating)
                    .build();

            eventRating = eventRatingRepository.save(eventRating);

            Double averageRating = calculateAverageRating(eventId);

            event.setAverageRating(averageRating);
            eventService.save(event);

            return eventRating;
        }  catch (Exception e) {
            logger.logError("Error creating rating: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating rating", e);
        }
    }

    private Double calculateAverageRating(Long eventId) {
        List<EventRating> ratings = eventRatingRepository.findByEventId(eventId);
        if (ratings.isEmpty()) {
            return 0.0;
        }

        double sum = ratings.stream().mapToInt(EventRating::getRating).sum();
        return sum / ratings.size();
    }

    private boolean hasUserRatedEvent(Long eventId, Long userId) {
        return eventRatingRepository.existsByEventIdAndRaterId(eventId, userId);
    }
}

