package backend343.controller;

import java.util.List;

import backend343.models.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import backend343.models.Schedule;
import backend343.service.ScheduleService;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/all-schedules")
    public List<Schedule> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @PostMapping("/create/{eventId}")
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule, @PathVariable("eventId") Long eventId) {
       return ResponseEntity.ok(scheduleService.createSchedule(schedule,eventId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable("id") Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    //endpoint to get eventId from a schedule
    @GetMapping("/{id}/event")
    public ResponseEntity<Long> getEventIdFromSchedule(@PathVariable("id") Long id){
        Long eventId = scheduleService.getEventIdFromSchedule(id);
        return ResponseEntity.ok(eventId);
    }

    @GetMapping("/{id}/sessions")
    public List<Session> getSessionsFromSchedule(@PathVariable("id") Long id){
        return scheduleService.getScheduleById(id).getSessions();
    }

}
