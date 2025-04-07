package backend343.service;

import java.util.List;
import java.util.Optional;

import backend343.models.Event;
import backend343.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend343.models.Schedule;
import backend343.repository.ScheduleRepository;

@Service
public class ScheduleService {
    
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private EventService eventService;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Schedule getScheduleById(Long id) {

        return scheduleRepository.findById(id).orElseThrow(() -> new RuntimeException("Schedule not found"));
    }

    public Schedule createSchedule(Schedule schedule, Long eventId) {
        Event event = eventService.findById(eventId);
        schedule.setEvent(event);
        return scheduleRepository.save(schedule);
    }

    public void save(Schedule schedule){
        scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id){
        scheduleRepository.deleteById(id);
    }

    public Long getEventIdFromSchedule(Long scheduleId){
        return scheduleRepository.findById(scheduleId)
        .map(schedule -> schedule.getEvent().getId())
        .orElseThrow(() -> new RuntimeException("Schedule not found"));
    }
    public List<Schedule> getSchedulesForEvent(Long eventId) {
        return scheduleRepository.findByEventId(eventId);
    }

}
