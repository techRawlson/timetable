package com.TimeTable.Controller;

import com.TimeTable.Entities.LectureData;
import com.TimeTable.Entities.Timetable;
import com.TimeTable.Entities.UpdateTimetableRequest;
import com.TimeTable.Repository.LectureDataRepository;
import com.TimeTable.Services.LectureDataService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/LockedData")
public class LectureDataController {
    @Autowired
    private LectureDataService lectureService;

    @Autowired
    private LectureDataRepository lectureDataRepository;



    @PostMapping("/teachersData")
    public ResponseEntity<LectureData> createLecture(@RequestBody LectureData lecture) {
        LectureData createdLecture = lectureService.createLecture(lecture);
        return new ResponseEntity<>(createdLecture, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteId(@PathVariable Long id){
        try {
            lectureDataRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @DeleteMapping("/{className}/{section}/{session}/{lectureNumber}")
    public ResponseEntity<String> deleteTimetableEntry(
            @PathVariable String className,
            @PathVariable String section,
            @PathVariable String session,
            @PathVariable int lectureNumber) {

        try {
            lectureDataRepository.deleteByClassNameAndSectionAndSessionAndLectureNumber(className, section, session, lectureNumber);
            return new ResponseEntity<>("Entry deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete entry", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/lecturedata")
    public List<LectureData> getAllLectureData() {
        return lectureService.getAllLectureData();
    }
    @PutMapping("/update-Staff-Timetable/{className}/{section}/{session}/{lectureNumber}/{day}")
    public ResponseEntity<LectureData> updateTimetable(@PathVariable String className,
                                                     @PathVariable String section,
                                                     @PathVariable String session,
                                                     @PathVariable int lectureNumber,
                                                     @PathVariable String day,
                                                     @RequestBody UpdateTimetableRequest requestBody) {
        try {
            // Find the timetable entry based on the provided parameters
            Optional<LectureData> existingTimetableOptional = lectureDataRepository.findByClassNameAndSectionAndSessionAndLectureNumberAndDay(
                    className, section, session, lectureNumber, day);

            if (existingTimetableOptional.isEmpty()) {
                // If the timetable entry does not exist, return not found
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Get the existing timetable entry
            LectureData existingTimetable = existingTimetableOptional.get();

            // Update the existing timetable entry with the new teacher name and subject
            String teacherName = requestBody.getTeacherName();
            // Check if the teacher name is empty, if so, set it to null
            if (teacherName != null && teacherName.trim().isEmpty()) {
                teacherName = null;
            }
            existingTimetable.setTeacherName(teacherName);
            existingTimetable.setTeacherSubject(requestBody.getSubject());

            // Save the updated timetable entry
            LectureData updatedTimetable = lectureDataRepository.save(existingTimetable);
            return new ResponseEntity<>(updatedTimetable, HttpStatus.OK);
        } catch (Exception e) {
            // If an error occurs, return internal server error
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/get-timetable/{session}/{className}/{section}/{day}")
    public ResponseEntity<List<LectureData>> getTimetableBySessionClassNameSectionAndDay(
            @PathVariable String session,
            @PathVariable String className,
            @PathVariable String section,
            @PathVariable String day) {
        List<LectureData> lectureData = lectureService.getTimetableBySessionClassNameSectionAndDay(session, className, section, day);
        return ResponseEntity.ok(lectureData);
    }
}
