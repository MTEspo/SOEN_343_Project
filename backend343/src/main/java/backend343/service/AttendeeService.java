package backend343.service;

import backend343.enums.Tag;
import backend343.models.Attendee;
import backend343.repository.AttendeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendeeService {

    private final AttendeeRepository attendeeRepository;

    public List<Tag> getAttendeeInterests(Long id) {
        Attendee attendee = attendeeRepository.findById(id).orElseThrow();
        return attendee.getInterests();
    }

    public Attendee getAttendeeById(Long id) {
        return attendeeRepository.findById(id).orElseThrow();
    }
}
