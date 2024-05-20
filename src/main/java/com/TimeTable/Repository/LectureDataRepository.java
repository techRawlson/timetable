package com.TimeTable.Repository;

import com.TimeTable.Entities.LectureData;
import com.TimeTable.Entities.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureDataRepository extends JpaRepository<LectureData, Long> {
    boolean existsByTeacherNameAndDayAndLectureNumber(String teacherName, String day, int lectureNumber);
    @Transactional
    @Modifying
    @Query("DELETE FROM LectureData d WHERE d.className = :className AND d.section = :section AND d.session = :session AND d.lectureNumber = :lectureNumber")
    void deleteByClassNameAndSectionAndSessionAndLectureNumber(String className, String section, String session, int lectureNumber);
    Optional<LectureData> findByClassNameAndSectionAndSessionAndLectureNumberAndDay(String className, String section, String session, int lectureNumber, String day);
    List<LectureData> findBySessionAndClassNameAndSectionAndDay(String session, String className, String section, String day);

}
