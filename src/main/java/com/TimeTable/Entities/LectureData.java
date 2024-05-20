package com.TimeTable.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class LectureData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String teacherName;
    private String teacherSubject;
    private int lectureNumber;
    private String day;
    private String className;
    private String section;
    private String session;
    private LocalTime startTime;
    private LocalTime endTime;

}
