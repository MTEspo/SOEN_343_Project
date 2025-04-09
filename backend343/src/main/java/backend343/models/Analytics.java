package backend343.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class Analytics{
    private BigDecimal amountGenerated;
    private List<Speaker> speakers;
    private List<User> attendees;
    private Double averageRating;
    // probable add sponsor
    // add money given by sponsor
    // add all stakeholders
}
