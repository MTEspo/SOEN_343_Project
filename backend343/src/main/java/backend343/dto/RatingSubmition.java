package backend343.dto;

import lombok.Data;

@Data
public class RatingSubmition {
    private Long speakerId;
    private Long userId;
    private int rating;
    private String comment;
}
