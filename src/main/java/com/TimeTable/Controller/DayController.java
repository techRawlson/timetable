package com.TimeTable.Controller;

import com.TimeTable.Entities.DaysEntities;
import com.TimeTable.Repository.DayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/days")
public class DayController {
    @Autowired
    private DayRepository dayRepository;
    @PostMapping("/create-day")
    public ResponseEntity<DaysEntities> addDay(@RequestBody DaysEntities day) {
        try {
            // Check if the day already exists in the database
            Optional<DaysEntities> existingDay = dayRepository.findByDay(day.getDay());

            if (existingDay.isPresent()) {
                // If the day already exists, return conflict response
                return ResponseEntity.status(HttpStatus.CONFLICT).body(existingDay.get());
            }

            // Save the day entity
            DaysEntities newDay = dayRepository.save(day);
            return new ResponseEntity<>(newDay, HttpStatus.CREATED);
        } catch (Exception e) {
            // If an error occurs, return internal server error
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping("/days")
//    public ResponseEntity<List<DaysEntities>> getAllDaysOrdered() {
//        try {
//            // Fetch all days from the database
//            List<DaysEntities> allDays = dayRepository.findAll();
//
//            // Sort the days by their string representation
//            allDays.sort(Comparator.comparing(DaysEntities::getDay));
//
//            return ResponseEntity.ok().body(allDays);
//        } catch (Exception e) {
//            // If an error occurs, return internal server error
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }




    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePeriodById(@PathVariable long id) {
        try {
            dayRepository.deleteById(id);
            return new ResponseEntity<>("Day with ID " + id + " deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

//    @GetMapping("/switch-on")
//    public List<DaysEntities> getDaysWithSwitchOn() {
//        return dayRepository.findDaysWhereSwitchIsOn();
//    }
//
//    @GetMapping("/switch-off")
//    public List<DaysEntities> getDaysWithSwitchOff() {
//        return dayRepository.findDaysWhereSwitchIsOff();
//    }
@PutMapping("/{dayName}")
public ResponseEntity<String> updateSwitchStatus(@PathVariable String dayName, @RequestBody String switchStatus) {
    try {
        // Find the day by name
        DaysEntities day = dayRepository.findByDay(dayName)
                .orElseThrow(() -> new ResourceNotFoundException("Day not found with name: " + dayName));

        // Update switchStatus directly with the value from the request body
        day.setSwitchStatus(switchStatus);

        // Save the updated day
        dayRepository.save(day);

        return ResponseEntity.ok("SwitchStatus updated successfully for day with name: " + dayName);
    } catch (Exception e) {
        // If an error occurs, return internal server error
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error updating switchStatus for day with name: " + dayName);
    }
}




    @GetMapping("/days")
    public ResponseEntity<List<DaysEntities>> getAllDaysOrdered() {
        try {
            // Fetch all days from the database
            List<DaysEntities> allDays = dayRepository.findAll();

            // Filter out days where switch is off
            List<DaysEntities> visibleDays = allDays.stream()
                    .filter(day -> "on".equalsIgnoreCase(day.getSwitchStatus()))
                    .collect(Collectors.toList());

            // Sort the visible days by their string representation
            visibleDays.sort(Comparator.comparing(DaysEntities::getDay));

            return ResponseEntity.ok().body(visibleDays);
        } catch (Exception e) {
            // If an error occurs, return internal server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/days/{dayName}")
    public ResponseEntity<DaysEntities> getDayByDayName(@PathVariable String dayName) {
        try {
            // Find the day by day name
            DaysEntities day = dayRepository.findByDay(dayName)
                    .orElseThrow(() -> new ResourceNotFoundException("Day not found with name: " + dayName));

            return ResponseEntity.ok(day);
        } catch (ResourceNotFoundException e) {
            // If the day is not found, return 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            // If an error occurs, return internal server error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
