package com.TimeTable.Repository;



import com.TimeTable.Entities.Periods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface PeriodsRepository extends JpaRepository<Periods, Integer> {
    Periods findByLectureNumber(int lectureNumber);
    boolean existsByStartTimeAndEndTime(LocalTime startTime, LocalTime endTime);
    List<Periods> findAllByOrderByLectureNumberAsc();
    boolean existsByLectureNumber(int lectureNumber);
    List<Periods> findAllByStartTimeBeforeAndEndTimeAfter(LocalTime endTime, LocalTime startTime);


}
