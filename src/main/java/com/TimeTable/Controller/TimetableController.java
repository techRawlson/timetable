package com.TimeTable.Controller;

import com.TimeTable.Entities.Timetable;
import com.TimeTable.Repository.TimetableRepository;
import com.TimeTable.Services.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//http://localhost:8086/api/timetable...
@RestController
@RequestMapping("/api/timetable")
public class TimetableController {

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private TimetableService timetableService;

    // Get all timetable entries
    @GetMapping("/full-timetable")
    public List<Timetable> getAllTimetable() {
        List<Timetable> timetable = timetableRepository.findAll(Sort.by(Sort.Direction.ASC, "lectureNumber"));
        return timetable;
    }

    // Get a specific timetable entry by ID
    @GetMapping("/get/{id}")
    public ResponseEntity<Timetable> getTimetableById(@PathVariable Long id) {
        Optional<Timetable> timetable = timetableRepository.findById(id);
        return timetable.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create-timetable")
    public ResponseEntity<Timetable> addTimetable(@RequestBody Timetable timetable) {
        try {
            // Check if start time is smaller than end time
            if (timetable.getStartTime().isBefore(timetable.getEndTime())) {
                // Check if timetable with the same lecture number, session, class name, and section exists
                Optional<Timetable> existingTimetable = timetableRepository.findByLectureNumberAndSessionAndClassNameAndSection(
                        timetable.getLectureNumber(),
                        timetable.getSession(),
                        timetable.getClassName(),
                        timetable.getSection()
                );
                if (existingTimetable.isPresent()) {
                    // If timetable already exists, return conflict response
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                } else {
                    // If timetable doesn't exist, save it
                    Timetable newTimetable = timetableRepository.save(timetable);
                    return new ResponseEntity<>(newTimetable, HttpStatus.CREATED);
                }
            } else {
                // Return bad request if condition is not met
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






    // Update an existing timetable entry
    @PutMapping("/update/{id}")
    public ResponseEntity<Timetable> updateTimetable(@PathVariable Long id, @RequestBody Timetable timetable) {
        Optional<Timetable> timetableData = timetableRepository.findById(id);

        if (timetableData.isPresent()) {
            Timetable existingTimetable = timetableData.get();
            existingTimetable.setMonday(timetable.getMonday());
            existingTimetable.setTuesday(timetable.getTuesday());
            existingTimetable.setWednesday(timetable.getWednesday());
            existingTimetable.setThursday(timetable.getThursday());
            existingTimetable.setFriday(timetable.getFriday());

            return new ResponseEntity<>(timetableRepository.save(existingTimetable), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a timetable entry by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTimetable(@PathVariable Long id) {
        try {
            timetableRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/get-timetable/{session}/{className}/{section}")
    public ResponseEntity<List<Timetable>> getTimetableBySessionClassNameAndSection(
            @PathVariable String session,
            @PathVariable String className,
            @PathVariable String section) {
        List<Timetable> timetable = timetableService.getTimetableBySessionClassNameAndSection(session, className, section);
        return ResponseEntity.ok(timetable);
    }



}