package backend343.controller;

import backend343.enums.Tag;
import backend343.service.AttendeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendees")
@RequiredArgsConstructor
public class AttendeeController {
    private final AttendeeService attendeeService;

    @GetMapping("/{id}/interests")
    public List<Tag> getAttendeeInterests(@PathVariable Long id) {
        return attendeeService.getAttendeeInterests(id);
    }

}
