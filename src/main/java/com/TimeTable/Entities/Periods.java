package com.TimeTable.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
public class Periods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int lectureNumber;
    private LocalTime startTime;
    private LocalTime endTime;

}

