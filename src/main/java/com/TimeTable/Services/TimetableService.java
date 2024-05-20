package com.TimeTable.Services;

import com.TimeTable.Entities.DaysEntities;
import com.TimeTable.Entities.Timetable;
import com.TimeTable.Repository.DayRepository;
import com.TimeTable.Repository.TimetableRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;
   @Autowired
   private DayRepository dayRepository;
    @Autowired
    public TimetableService(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    public List<Timetable> getTimeTableByClassName(String className) {
        return timetableRepository.findByClassName(className);
    }

    public List<Timetable> getTimetableByClassNameAndSectionAndSession(String className, String section, String session) {
        List<Timetable> timetable = timetableRepository.findByClassNameAndSectionAndSession(className, section, session);

        return timetable; // Return the retrieved timetable
    }
    public List<Timetable> getTimetableBySessionClassNameAndSection(String session, String className, String section) {
        return timetableRepository.findBySessionAndClassNameAndSection(session, className, section);
    }
    public List<Timetable> getTimetableBySessionClassNameSectionAndDay(String session, String className, String section, String day) {
        return timetableRepository.findBySessionAndClassNameAndSectionAndDay(session, className, section, day);
    }
    @PostConstruct
    public void initializeDays() {
        List<String> daysOfWeek = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        // Iterate through each day of the week
        for (String day : daysOfWeek) {
            // Check if the day already exists in the database
            if (!dayRepository.findByDay(day).isPresent()) {
                // If not, create a new DayEntity with default status "on" and save it to the database
                DaysEntities newDay = new DaysEntities();
                newDay.setDay(day);
                newDay.setSwitchStatus("on");
                dayRepository.save(newDay);
            }
        }
    }

}
