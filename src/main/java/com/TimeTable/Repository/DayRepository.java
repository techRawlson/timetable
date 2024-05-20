package com.TimeTable.Repository;

import com.TimeTable.Entities.DaysEntities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface DayRepository  extends JpaRepository<DaysEntities,Long> {

    Optional<DaysEntities> findByDay(String day);
    @Query("SELECT d FROM DaysEntities d WHERE d.switchStatus = 'off'")
    List<DaysEntities> findDaysWhereSwitchIsOff();
}
