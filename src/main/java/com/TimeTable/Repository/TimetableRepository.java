package com.TimeTable.Repository;

import com.TimeTable.Entities.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByClassName(String className);
    @Query("SELECT t FROM Timetable t WHERE t.className = ?1 AND t.section = ?2 AND t.session = ?3")
    List<Timetable> findByClassNameAndSectionAndSession(String className, String section, String session);
    List<Timetable> findBySessionAndClassNameAndSection(String session, String className, String section);
    Optional<Timetable> findByLectureNumberAndSessionAndClassNameAndSection(
            int lectureNumber, String session, String className, String section);

}
