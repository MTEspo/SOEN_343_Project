package backend343.controller;

import backend343.dto.RatingSubmition;
import backend343.models.Rating;
import backend343.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    @PostMapping("/create-rating")
    public ResponseEntity<Rating> createRating(@RequestBody RatingSubmition ratingSubmition) {
        return ResponseEntity.ok(ratingService.createRating(ratingSubmition));
    }
}
