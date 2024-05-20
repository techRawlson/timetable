package com.TimeTable.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class DaysEntities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String day;
    private String switchStatus;
    public String getSwitchStatus() {
        // Trim leading and trailing whitespace from switchStatus
        return switchStatus.trim();
    }
}
