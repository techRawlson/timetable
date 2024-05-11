package com.TimeTable.Services;

import com.TimeTable.Entities.Timetable;
import com.TimeTable.Repository.TimetableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;

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



}
