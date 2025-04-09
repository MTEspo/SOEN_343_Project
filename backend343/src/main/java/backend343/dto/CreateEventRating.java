package backend343.dto;

import lombok.Data;

@Data
public class CreateEventRating {
    private Long eventId;
    private Long userId;
    private Integer rating;
}
