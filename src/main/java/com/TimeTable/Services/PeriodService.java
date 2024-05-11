package com.TimeTable.Services;


import com.TimeTable.Entities.Periods;
import com.TimeTable.Repository.PeriodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodService {
    @Autowired
    private PeriodsRepository periodsRepository;

    public Periods createLecture(Periods lecture) {
        return periodsRepository.save(lecture);
    }
    public List<Periods> getAllLectures() {
        return periodsRepository.findAll();
    }
    public void deletePeriodById(int id) {
        periodsRepository.deleteById(id);
    }

    // Method to update a period's startTime and endTime by its ID
    public void updatePeriod(int id, String startTime, String endTime) {
        Optional<Periods> optionalPeriod = periodsRepository.findById(id);
        if (optionalPeriod.isPresent()) {
            Periods period = optionalPeriod.get();
            period.setStartTime(LocalTime.parse(startTime));
            period.setEndTime(LocalTime.parse(endTime));
            periodsRepository.save(period);
        } else {
            // Handle the case where the period with the given ID is not found
            throw new RuntimeException("Period not found with id: " + id);
        }
    }
    public Periods findByLectureNumber(int lectureNumber) {
        return periodsRepository.findByLectureNumber(lectureNumber);
    }
    public List<Periods> getAllPeriodsSortedByLectureNumber() {
        return periodsRepository.findAllByOrderByLectureNumberAsc();
    }
}

