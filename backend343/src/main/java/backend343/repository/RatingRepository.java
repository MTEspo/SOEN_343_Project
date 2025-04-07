package backend343.repository;

import org.springframework.stereotype.Repository;
import backend343.models.Rating;
import backend343.models.Speaker;
import backend343.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findBySpeaker(Speaker speaker);

    @Query("SELECT r FROM Rating r WHERE r.rater = :rater AND r.speaker = :speaker")
    Rating findByRaterAndSpeaker(@Param("rater") User rater, @Param("speaker") Speaker speaker);
}
