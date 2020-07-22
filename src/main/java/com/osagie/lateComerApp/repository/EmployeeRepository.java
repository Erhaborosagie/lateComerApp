package com.osagie.lateComerApp.repository;

import com.osagie.lateComerApp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Modifying
    @Query("update Employee e set e.email = ?1, e.address = ?2 where e.id = ?3")
    void updateEmployee(String email, String address, Long id);

    @Query("SELECT e FROM Employee e WHERE e.name LIKE %?1% OR e.email LIKE %?1% OR e.address LIKE %?1% ")
    List<Employee> searchEmployee(String keyword);
}
