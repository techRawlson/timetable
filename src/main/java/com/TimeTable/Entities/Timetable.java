package com.TimeTable.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


    @Entity
    @Data
    public class Timetable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String day;
        private String teacherName;
        private String subject;
        private LocalTime startTime;
        private LocalTime endTime;
        private int lectureNumber;
        private String session;
        private String className;
        private String section;

    }

