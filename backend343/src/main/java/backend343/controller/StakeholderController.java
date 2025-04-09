package backend343.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import backend343.models.Event;
import backend343.models.Stakeholder;
import backend343.service.EventService;
import backend343.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stakeholders")
@RequiredArgsConstructor
public class StakeholderController {

    private final EventService eventService;
    private final UserDetailsServiceImpl userDetailsService;

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEventsForInvestment() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    //stakeholder invests in an eventski
    @PostMapping("/{stakeholderId}/invest/{eventId}")
    public ResponseEntity<String> investInEvent(
            @PathVariable("stakeholderId") Long stakeholderId,
            @PathVariable("eventId") Long eventId,
            @RequestParam("amount") BigDecimal amount) {

        Stakeholder stakeholder = (Stakeholder) userDetailsService.getUserById(stakeholderId);
        boolean success = eventService.handleStakeholderInvestment(stakeholder, eventId, amount);

        if (success) {
            return ResponseEntity.ok("Investment successful.");
        } else {
            return ResponseEntity.badRequest().body("Investment failed.");
        }
    }

    @GetMapping("/{stakeholderId}/invested-events")
    public ResponseEntity<List<Map<String, Object>>> getInvestedEvents(@PathVariable("stakeholderId") Long stakeholderId) {
        List<Map<String, Object>> investments = eventService.getInvestmentsByStakeholder(stakeholderId);
        return ResponseEntity.ok(investments);
    }
    
}
