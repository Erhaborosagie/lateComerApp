package com.osagie.lateComerApp.service;


import com.osagie.lateComerApp.dto.EmployeeDto;
import com.osagie.lateComerApp.model.Employee;
import org.springframework.data.domain.Page;

import java.util.List;

public interface EmployeeService {
    Employee addEmployee(EmployeeDto employeeDto);
    Employee updateEmployee(Employee employee);
    void deleteEmployee(Long id);
    Employee findById(Long id);

    Page<Employee> findAll(int page, int size, String param, String dir);
    List<Employee> searchEmployee(String keyword);

    List<Employee> findAll();
}
