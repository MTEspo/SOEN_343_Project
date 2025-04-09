package backend343.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class Analytics{
    private BigDecimal amountGenerated;
    private BigDecimal stakeholderContributions; // NEW: total from stakeholders
    private BigDecimal totalRevenue;             // amountGenerated + stakeholderContributions
    private BigDecimal fundingGoal;
    private List<Speaker> speakers;
    private List<User> attendees;
    private List<Stakeholder> stakeholders;      // who invested
}
