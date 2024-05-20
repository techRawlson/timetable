package com.TimeTable.Controller;


import com.TimeTable.Entities.Periods;

import com.TimeTable.Repository.PeriodsRepository;
import com.TimeTable.Repository.TimetableRepository;
import com.TimeTable.Services.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/periods")
public class PeriodsController {
    @Autowired
    private PeriodService service;
    @Autowired
    private TimetableRepository timetableRepository;
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
            int expectedLectureNumber = (int) periodsRepository.count() + 1; // Expecting the next lecture number
            if (newPeriods.getLectureNumber() != expectedLectureNumber) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lecture number must be sequential");
            }

            // Check if the lecture number is directly after the last saved period's lecture number
            Optional<Periods> optionalLastSavedPeriod = periodsRepository.findFirstByOrderByLectureNumberDesc();
            if (optionalLastSavedPeriod.isPresent() && newPeriods.getLectureNumber() != optionalLastSavedPeriod.get().getLectureNumber() + 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lecture numbers must be sequential");
            }

            // Proceed with saving the period if lecture numbers are sequential
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
            // Find the period associated with the given ID
            Optional<Periods> optionalPeriod = periodsRepository.findById(id);

            if (!optionalPeriod.isPresent()) {
                // If the period ID doesn't exist, return not found
                return new ResponseEntity<>("Period with ID " + id + " not found", HttpStatus.NOT_FOUND);
            }

            Periods period = optionalPeriod.get();
            int lectureNumber = period.getLectureNumber();

            // Check if any subsequent periods exist with a higher lecture number
            boolean subsequentPeriodExists = periodsRepository.existsByLectureNumberGreaterThan(lectureNumber);

            if (subsequentPeriodExists) {
                // If subsequent periods exist, return a conflict response
                return new ResponseEntity<>("Cannot delete period with ID " + id + " because subsequent periods exist", HttpStatus.CONFLICT);
            }

            // Check if the lecture number is used in the Timetable repository
            boolean isLectureUsed = timetableRepository.existsByLectureNumber(lectureNumber);

            if (isLectureUsed) {
                // If the lecture is used, return a conflict response
                return new ResponseEntity<>("Period with ID " + id + " is associated with a lecture and cannot be deleted", HttpStatus.CONFLICT);
            }

            // If the period is not associated with a lecture and no subsequent periods exist, proceed with deletion
            periodsRepository.deleteById(id);
            return new ResponseEntity<>("Period with ID " + id + " deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
