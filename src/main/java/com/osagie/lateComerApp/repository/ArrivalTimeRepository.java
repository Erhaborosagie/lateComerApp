package com.osagie.lateComerApp.repository;

import com.osagie.lateComerApp.model.ArrivalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArrivalTimeRepository extends JpaRepository<ArrivalTime, Long> {
    Optional<ArrivalTime> findTopByEmployeesIdOrderByClockingTimeAsc(Long employees_id);
}
