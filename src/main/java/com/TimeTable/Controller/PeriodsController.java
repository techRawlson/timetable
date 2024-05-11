package com.TimeTable.Controller;


import com.TimeTable.Entities.Periods;

import com.TimeTable.Repository.PeriodsRepository;
import com.TimeTable.Services.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periods")
public class PeriodsController {
    @Autowired
    private PeriodService service;
    @Autowired
    private PeriodsRepository periodsRepository;
//    @PostMapping("/create-periods")
//    public ResponseEntity<Periods> createLecture(@RequestBody Periods lecture) {
//        try {
//            if(lecture.getStartTime().isBefore(lecture.getEndTime())){
//                Periods newPeriods = periodsRepository.save(lecture);
//                return new ResponseEntity<>(newPeriods, HttpStatus.CREATED);
//            }else {
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//        }
//        catch (Exception e){
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//@PostMapping("/create-periods")
//public ResponseEntity<?> createPeriods(@RequestBody Periods newPeriods) {
//    try {
//        // Check if the lecture number already exists
//        if (periodsRepository.existsByLectureNumber(newPeriods.getLectureNumber())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lecture number already exists");
//        }
//
//        // Check if the time period already exists
//        if (periodsRepository.existsByStartTimeAndEndTime(newPeriods.getStartTime(), newPeriods.getEndTime())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Time period already exists");
//        }
//
//        // Check if the time slot overlaps with existing periods
//        List<Periods> existingPeriods = periodsRepository.findAllByStartTimeBeforeAndEndTimeAfter(newPeriods.getEndTime(), newPeriods.getStartTime());
//        for (Periods period : existingPeriods) {
//            if (period.getStartTime().isBefore(newPeriods.getEndTime()) && period.getEndTime().isAfter(newPeriods.getStartTime())) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Time slot overlaps with existing period");
//            }
//        }
//
//        // Check if start time is before end time
//        if (newPeriods.getStartTime().isBefore(newPeriods.getEndTime())) {
//            Periods savedPeriods = periodsRepository.save(newPeriods);
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedPeriods);
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End time must be after start time");
//        }
//    } catch (Exception e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//    }
//}
@PostMapping("/create-periods")
public ResponseEntity<?> createPeriods(@RequestBody Periods newPeriods) {
    try {
        // Check if the lecture number already exists
        if (periodsRepository.existsByLectureNumber(newPeriods.getLectureNumber())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lecture number already exists");
        }

        // Check if the time period already exists
        if (periodsRepository.existsByStartTimeAndEndTime(newPeriods.getStartTime(), newPeriods.getEndTime())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Time period already exists");
        }

        // Check if the time slot overlaps with existing periods
        List<Periods> existingPeriods = periodsRepository.findAllByStartTimeBeforeAndEndTimeAfter(newPeriods.getEndTime(), newPeriods.getStartTime());
        for (Periods period : existingPeriods) {
            if (period.getStartTime().isBefore(newPeriods.getEndTime()) && period.getEndTime().isAfter(newPeriods.getStartTime())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Time slot overlaps with existing period");
            }
        }

        // Check if start time is before end time
        if (newPeriods.getStartTime().isBefore(newPeriods.getEndTime())) {
            // Check if the lecture number is sequential
            int expectedLectureNumber =(int) periodsRepository.count() + 1; // Expecting the next lecture number
            if (newPeriods.getLectureNumber() != expectedLectureNumber) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lecture number must be sequential");
            }

            Periods savedPeriods = periodsRepository.save(newPeriods);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedPeriods);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End time must be after start time");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}



    //    @GetMapping("/lectures")
//    public ResponseEntity<List<Periods>> getAllLectures() {
//        List<Periods> lectures = service.getAllLectures();
//        return new ResponseEntity<>(lectures, HttpStatus.OK);
//    }
@GetMapping("/lectures")
public ResponseEntity<List<Periods>> getAllPeriodsSortedByLectureNumber() {
    List<Periods> periods = service.getAllPeriodsSortedByLectureNumber();
    return ResponseEntity.ok(periods);
}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePeriodById(@PathVariable int id) {
        try {
            service.deletePeriodById(id);
            return new ResponseEntity<>("Period with ID " + id + " deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to update a period's startTime and endTime by its ID
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePeriod(@PathVariable int id, @RequestParam String startTime, @RequestParam String endTime) {
        try {
            service.updatePeriod(id, startTime, endTime);
            return new ResponseEntity<>("Period with ID " + id + " updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/lecture/{lectureNumber}")
    public Periods getLectureByNumber(@PathVariable int lectureNumber) {
        return service.findByLectureNumber(lectureNumber);
    }


}
