package backend343.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend343.models.Schedule;
import backend343.repository.ScheduleRepository;

@Service
public class ScheduleService {
    
    @Autowired
    private ScheduleRepository scheduleRepository;

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    public Optional<Schedule> getScheduleById(Long id) {
        return scheduleRepository.findById(id);
    }

    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    public void deleteSchedule(Long id){
        scheduleRepository.deleteById(id);
    }
}
