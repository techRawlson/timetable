package com.TimeTable.Entities;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
@Nullable
public class UpdateTimetableRequest {
    private String teacherName;
    private String subject;

}
