package com.TimeTable.Controller;

import com.TimeTable.Entities.Timetable;
import com.TimeTable.Entities.UpdateTimetableRequest;
import com.TimeTable.Repository.LectureDataRepository;
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
    private LectureDataRepository lectureDataRepository;

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

//    @PostMapping("/create-timetable")
//    public ResponseEntity<Timetable> addTimetable(@RequestBody Timetable timetable) {
//        try {
//            // Check if start time is smaller than end time
//            if (timetable.getStartTime().isBefore(timetable.getEndTime())) {
//                // Check if timetable with the same lecture number, session, class name, and section exists
//                Optional<Timetable> existingTimetable = timetableRepository.findByLectureNumberAndSessionAndClassNameAndSection(
//                        timetable.getLectureNumber(),
//                        timetable.getSession(),
//                        timetable.getClassName(),
//                        timetable.getSection()
//                );
//                if (existingTimetable.isPresent()) {
//                    // If timetable already exists, return conflict response
//                    return new ResponseEntity<>(HttpStatus.CONFLICT);
//                } else {
//                    // If timetable doesn't exist, save it
//                    Timetable newTimetable = timetableRepository.save(timetable);
//                    return new ResponseEntity<>(newTimetable, HttpStatus.CREATED);
//                }
//            } else {
//                // Return bad request if condition is not met
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping("/create-timetable")
//    public ResponseEntity<Timetable> addTimetable(@RequestBody Timetable timetable) {
//        try {
//            // Check if start time is smaller than end time
//            if (timetable.getStartTime().isBefore(timetable.getEndTime())) {
//                // Check if at least one entry is present for each day
//                if (timetable.getMonday() == null || timetable.getTuesday() == null ||
//                        timetable.getWednesday() == null || timetable.getThursday() == null ||
//                        timetable.getFriday() == null) {
//                    // If any of the days is null, return bad request
//                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//                }
//
//                // Check if timetable with the same lecture number, session, class name, and section exists
//                Optional<Timetable> existingTimetable = timetableRepository.findByLectureNumberAndSessionAndClassNameAndSection(
//                        timetable.getLectureNumber(),
//                        timetable.getSession(),
//                        timetable.getClassName(),
//                        timetable.getSection()
//                );
//                if (existingTimetable.isPresent()) {
//                    // If timetable already exists, return conflict response
//                    return new ResponseEntity<>(HttpStatus.CONFLICT);
//                } else {
//                    // Check if lecture number is sequential
//                    int expectedLectureNumber = (int) timetableRepository.count() + 1; // Expecting the next lecture number
//                    if (timetable.getLectureNumber() != expectedLectureNumber) {
//                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//                    }
//
//                    // If lecture number is not 1 and there's no timetable with lecture number 1, return bad request
//                    if (timetable.getLectureNumber() != 1 && !timetableRepository.existsByLectureNumber(1)) {
//                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//                    }
//
//                    // If timetable doesn't exist and lecture number is sequential, save it
//                    Timetable newTimetable = timetableRepository.save(timetable);
//                    return new ResponseEntity<>(newTimetable, HttpStatus.CREATED);
//                }
//            } else {
//                // Return bad request if condition is not met
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping("/create-timetable")
//    public ResponseEntity<Timetable> addTimetable(@RequestBody Timetable timetable) {
//        try {
//            // Check if all required fields are present
//            if (timetable.getDay() == null || timetable.getTeacherName() == null ||
//                    timetable.getSubject() == null || timetable.getStartTime() == null ||
//                    timetable.getEndTime() == null || timetable.getSession() == null ||
//                    timetable.getClassName() == null || timetable.getSection() == null) {
//                // If any required field is missing, return bad request
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            // Check if timetable with the same lecture number, session, class name, and section exists
//            boolean exists = timetableRepository.existsByLectureNumberAndSessionAndClassNameAndSection(
//                    timetable.getLectureNumber(),
//                    timetable.getSession(),
//                    timetable.getClassName(),
//                    timetable.getSection()
//            );
//
//            if (exists) {
//                // If timetable already exists, return conflict response
//                return new ResponseEntity<>(HttpStatus.CONFLICT);
//            }
//
//            // Check if lecture number is sequential
//            int expectedLectureNumber = timetableRepository.findMaxLectureNumberBySessionAndClassNameAndSection(
//                    timetable.getSession(),
//                    timetable.getClassName(),
//                    timetable.getSection()
//            ) + 1; // Expecting the next lecture number
//
//            if (timetable.getLectureNumber() != expectedLectureNumber) {
//                // If the lecture number is not sequential, return bad request
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            // Save the timetable entry
//            Timetable newTimetable = timetableRepository.save(timetable);
//            return new ResponseEntity<>(newTimetable, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // If an error occurs, return internal server error
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    @PostMapping("/create-timetable")
//    public ResponseEntity<Timetable> addTimetable(@RequestBody Timetable timetable) {
//        try {
//            // Check if all required fields are present
//            if (timetable.getDay() == null || timetable.getTeacherName() == null ||
//                    timetable.getSubject() == null || timetable.getStartTime() == null ||
//                    timetable.getEndTime() == null || timetable.getSession() == null ||
//                    timetable.getClassName() == null || timetable.getSection() == null) {
//                // If any required field is missing, return bad request
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            // Check if timetable with the same lecture number, session, class name, and section exists
//            boolean exists = timetableRepository.existsByLectureNumberAndSessionAndClassNameAndSection(
//                    timetable.getLectureNumber(),
//                    timetable.getSession(),
//                    timetable.getClassName(),
//                    timetable.getSection()
//            );
//
//            if (exists) {
//                // If timetable already exists, return conflict response
//                return new ResponseEntity<>(HttpStatus.CONFLICT);
//            }
//
//            // Check if lecture number is sequential
//            int expectedLectureNumber = timetableRepository.findMaxLectureNumberBySessionAndClassNameAndSection(
//                    timetable.getSession(),
//                    timetable.getClassName(),
//                    timetable.getSection()
//            ) + 1; // Expecting the next lecture number
//
//            if (timetable.getLectureNumber() != expectedLectureNumber) {
//                // If the lecture number is not sequential, return bad request
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            // Check if the teacher is available for the given lecture
//            boolean teacherAvailable = lectureDataRepository.existsByTeacherNameAndSessionAndClassNameAndSection(
//                    timetable.getTeacherName(),
//                    timetable.getSession(),
//                    timetable.getClassName(),
//                    timetable.getSection()
//            );
//
//            if (!teacherAvailable) {
//                // If the teacher is not available for the given lecture, return bad request
//                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//            }
//
//            // Save the timetable entry
//            Timetable newTimetable = timetableRepository.save(timetable);
//            return new ResponseEntity<>(newTimetable, HttpStatus.CREATED);
//        } catch (Exception e) {
//            // If an error occurs, return internal server error
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }










    // Update an existing timetable entry
//    @PutMapping("/update-timetable/{className}/{section}/{session}/{lectureNumber}/{day}")
//    public ResponseEntity<Timetable> updateTimetable(@PathVariable String className,
//                                                     @PathVariable String section,
//                                                      @PathVariable String session,
//                                                     @PathVariable int lectureNumber,
//                                                      @PathVariable String day,
//                                                     @RequestBody UpdateTimetableRequest requestBody) {
//        try {
//            // Find the timetable entry based on the provided parameters
//            Optional<Timetable> existingTimetableOptional = timetableRepository.findByClassNameAndSectionAndSessionAndLectureNumberAndDay(
//                    className, section, session, lectureNumber, day);
//
//            if (existingTimetableOptional.isEmpty()) {
//                // If the timetable entry does not exist, return not found
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//
//            // Get the existing timetable entry
//            Timetable existingTimetable = existingTimetableOptional.get();
//
//            // Update the existing timetable entry with the new teacher name and subject
//            existingTimetable.setTeacherName(requestBody.getTeacherName());
//            existingTimetable.setSubject(requestBody.getSubject());
//
//            // Save the updated timetable entry
//            Timetable updatedTimetable = timetableRepository.save(existingTimetable);
//            return new ResponseEntity<>(updatedTimetable, HttpStatus.OK);
//        } catch (Exception e) {
//            // If an error occurs, return internal server error
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @PutMapping("/update-timetable/{className}/{section}/{session}/{lectureNumber}/{day}")
    public ResponseEntity<Timetable> updateTimetable(@PathVariable String className,
                                                     @PathVariable String section,
                                                     @PathVariable String session,
                                                     @PathVariable int lectureNumber,
                                                     @PathVariable String day,
                                                     @RequestBody UpdateTimetableRequest requestBody) {
        try {
            // Find the timetable entry based on the provided parameters
            Optional<Timetable> existingTimetableOptional = timetableRepository.findByClassNameAndSectionAndSessionAndLectureNumberAndDay(
                    className, section, session, lectureNumber, day);

            if (existingTimetableOptional.isEmpty()) {
                // If the timetable entry does not exist, return not found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Get the existing timetable entry
            Timetable existingTimetable = existingTimetableOptional.get();

            // Update the existing timetable entry with the new teacher name and subject
            String teacherName = requestBody.getTeacherName();
            // Check if the teacher name is empty, if so, set it to null
            if (teacherName != null && teacherName.trim().isEmpty()) {
                teacherName = null;
            }
            existingTimetable.setTeacherName(teacherName);
            existingTimetable.setSubject(requestBody.getSubject());

            // Save the updated timetable entry
            Timetable updatedTimetable = timetableRepository.save(existingTimetable);
            return new ResponseEntity<>(updatedTimetable, HttpStatus.OK);
        } catch (Exception e) {
            // If an error occurs, return internal server error
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    // Delete a timetable entry by ID
    @DeleteMapping("/{className}/{section}/{session}/{lectureNumber}")
    public ResponseEntity<String> deleteTimetableEntry(
            @PathVariable String className,
            @PathVariable String section,
            @PathVariable String session,
            @PathVariable int lectureNumber) {

        try {
            timetableRepository.deleteByClassNameAndSectionAndSessionAndLectureNumber(className, section, session, lectureNumber);
            return new ResponseEntity<>("Entry deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete entry", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/get-timetable/{session}/{className}/{section}/{day}")
    public ResponseEntity<List<Timetable>> getTimetableBySessionClassNameSectionAndDay(
            @PathVariable String session,
            @PathVariable String className,
            @PathVariable String section,
            @PathVariable String day) {
        List<Timetable> timetable = timetableService.getTimetableBySessionClassNameSectionAndDay(session, className, section, day);
        return ResponseEntity.ok(timetable);
    }

//    @PostMapping("/create-timetable")
//    public ResponseEntity<String> createTimetableEntry(@RequestBody Timetable timetable) {
//        try {
//            // Validate input
//            if (timetable.getDay() == null || timetable.getTeacherName() == null ||
//                    timetable.getSubject() == null || timetable.getStartTime() == null ||
//                    timetable.getEndTime() == null || timetable.getLectureNumber() <= 0 ||
//                    timetable.getSession() == null || timetable.getClassName() == null ||
//                    timetable.getSection() == null) {
//                return ResponseEntity.badRequest().body("Missing or invalid fields");
//            }
//
//            // Check if the timetable entry already exists for the same lecture number, class, section, and session
//            boolean entryExists = timetableRepository.existsByLectureNumberAndClassNameAndSectionAndSession(
//                    timetable.getLectureNumber(),
//                    timetable.getClassName(),
//                    timetable.getSection(),
//                    timetable.getSession()
//            );
//
//            if (entryExists) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("Timetable entry already exists for the same lecture number, class, section, and session");
//            }
//
//            // Check if the current lecture number follows the last created lecture number
//            int lastLectureNumber = timetableRepository.findMaxLectureNumberBySessionAndClassNameAndSection(
//                    timetable.getSession(),
//                    timetable.getClassName(),
//                    timetable.getSection()
//            );
//
//            if (timetable.getLectureNumber() != lastLectureNumber + 1) {
//                return ResponseEntity.badRequest().body("Lecture numbers must be sequential");
//            }
//
//            // Save the timetable entry
//            timetableRepository.save(timetable);
//            return ResponseEntity.status(HttpStatus.CREATED).body("Timetable entry created successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create timetable entry");
//        }
//    }


    @PostMapping("/create-timetable")
    public ResponseEntity<String> createTimetableEntry(@RequestBody Timetable timetable) {
        try {
            // Validate input
            if (timetable.getDay() == null || timetable.getStartTime() == null ||
                    timetable.getEndTime() == null || timetable.getLectureNumber() <= 0 ||
                    timetable.getSession() == null || timetable.getClassName() == null ||
                    timetable.getSection() == null) {
                return ResponseEntity.badRequest().body("Missing or invalid fields");
            }

            // Check if subject is provided
            if (timetable.getSubject() == null || timetable.getSubject().isEmpty()) {
                return ResponseEntity.badRequest().body("Subject is required");
            }

            // Check if both teacher and subject are provided
            if (timetable.getTeacherName() == null || timetable.getTeacherName().isEmpty()) {
                return ResponseEntity.badRequest().body("Teacher is required");
            }

            // Check if the timetable entry already exists for the same lecture number, class, section, and session
            boolean entryExists = timetableRepository.existsByLectureNumberAndClassNameAndSectionAndSession(
                    timetable.getLectureNumber(),
                    timetable.getClassName(),
                    timetable.getSection(),
                    timetable.getSession()
            );

            if (entryExists) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Timetable entry already exists for the same lecture number, class, section, and session");
            }

            // Check if the current lecture number follows the last created lecture number
            int lastLectureNumber = timetableRepository.findMaxLectureNumberBySessionAndClassNameAndSection(
                    timetable.getSession(),
                    timetable.getClassName(),
                    timetable.getSection()
            );

            if (timetable.getLectureNumber() != lastLectureNumber + 1) {
                return ResponseEntity.badRequest().body("Lecture numbers must be sequential");
            }

            // Check if the teacher is already assigned for the same lecture and day
            boolean teacherAssigned = lectureDataRepository.existsByTeacherNameAndDayAndLectureNumber(
                    timetable.getTeacherName(),
                    timetable.getDay(),
                    timetable.getLectureNumber()
            );

            if (teacherAssigned) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Teacher is already assigned for the same lecture and day");
            }

            // Save the timetable entry
            timetableRepository.save(timetable);
            return ResponseEntity.status(HttpStatus.CREATED).body("Timetable entry created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create timetable entry");
        }
    }


}