package com.TimeTable.Repository;

import com.TimeTable.Entities.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByClassName(String className);
    @Query("SELECT t FROM Timetable t WHERE t.className = ?1 AND t.section = ?2 AND t.session = ?3")

    List<Timetable> findByClassNameAndSectionAndSession(String className, String section, String session);
    List<Timetable> findBySessionAndClassNameAndSection(String session, String className, String section);
    Optional<Timetable> findByClassNameAndSectionAndSessionAndLectureNumberAndDay(String className, String section, String session, int lectureNumber, String day);

    boolean existsByLectureNumberAndSessionAndClassNameAndSection(
            int lectureNumber, String session, String className, String section);

    @Query("SELECT COALESCE(MAX(t.lectureNumber), 0) FROM Timetable t " +
            "WHERE t.session = :session AND t.className = :className AND t.section = :section")
    int findMaxLectureNumberBySessionAndClassNameAndSection(
            String session, String className, String section);
    Timetable findByLectureNumberAndDayAndClassNameAndSectionAndSession(
            int lectureNumber, String day, String className, String section, String session);

    List<Timetable> findBySessionAndClassNameAndSectionAndDay(String session, String className, String section, String day);
    boolean existsByLectureNumber(int lectureNumber);
    boolean existsByClassNameAndSectionAndSessionAndLectureNumber(String className, String section, String session, int lectureNumber);

    @Transactional
    @Modifying
    @Query("DELETE FROM Timetable t WHERE t.className = :className AND t.section = :section AND t.session = :session AND t.lectureNumber = :lectureNumber")
    void deleteByClassNameAndSectionAndSessionAndLectureNumber(String className, String section, String session, int lectureNumber);

    boolean existsByLectureNumberAndClassNameAndSectionAndSession(int lectureNumber, String className, String section, String session);

}
