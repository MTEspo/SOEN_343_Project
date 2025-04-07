package backend343.service;

import backend343.dto.RatingSubmition;
import backend343.models.Rating;
import backend343.models.Speaker;
import backend343.models.User;
import backend343.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final SpeakerService speakerService;
    private final UserDetailsServiceImpl userDetailsService;

    public Rating createRating(RatingSubmition ratingSubmition) {
        Speaker speaker = speakerService.findById(ratingSubmition.getSpeakerId());
        User rater = userDetailsService.getUserById(ratingSubmition.getUserId());
        Rating rating = Rating.builder()
                .speaker(speaker)
                .rater(rater)
                .rating(ratingSubmition.getRating())
                .comment(ratingSubmition.getComment())
                .build();
        rating = ratingRepository.save(rating);
        updateSpeakerAverageRating(speaker);
        return rating;
    }

    private void updateSpeakerAverageRating(Speaker speaker) {
        List<Rating> ratings = findBySpeaker(speaker);
        Double averageRating = ratings.stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(0.0);
        speaker.setAverageRating(averageRating);
        speakerService.updateSpeaker(speaker);
    }

    public Rating findById(Long id) {
        return ratingRepository.findById(id).orElseThrow();
    }

    public List<Rating> findBySpeaker(Speaker speaker) {
        return ratingRepository.findBySpeaker(speaker);
    }


}
