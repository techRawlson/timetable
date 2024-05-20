package com.TimeTable.Services;

import com.TimeTable.Entities.LectureData;
import com.TimeTable.Entities.Timetable;
import com.TimeTable.Repository.LectureDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LectureDataService {
    @Autowired
    private  LectureDataRepository lectureRepository;

    public LectureData createLecture(LectureData lecture) {
        // Add any business logic/validation before saving the lecture
        return lectureRepository.save(lecture);
    }
    public List<LectureData> getAllLectureData() {
        return lectureRepository.findAll();
    }
    public List<LectureData> getTimetableBySessionClassNameSectionAndDay(String session, String className, String section, String day) {
        return lectureRepository.findBySessionAndClassNameAndSectionAndDay(session, className, section, day);
    }
}
