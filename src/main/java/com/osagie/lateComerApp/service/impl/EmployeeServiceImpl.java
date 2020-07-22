package com.osagie.lateComerApp.service.impl;

import com.osagie.lateComerApp.dto.EmployeeDto;
import com.osagie.lateComerApp.model.Employee;
import com.osagie.lateComerApp.repository.EmployeeRepository;
import com.osagie.lateComerApp.service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    @Override
    public Employee addEmployee(EmployeeDto employeeDto) {
        Employee employee = employeeDto.toEmployee();
        employeeRepository.saveAndFlush(employee);
        return employee;
    }

    @Override
    public Employee findById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Employee> findAll(int page, int size, String param, String dir) {
        if (dir.equals("asc"))
            return employeeRepository.findAll(PageRequest.of(page, size, Sort.by(param).ascending()));
        return employeeRepository.findAll(PageRequest.of(page, size, Sort.by(param).descending()));
    }

    @Override
    public List<Employee> searchEmployee(String keyword) {
        return employeeRepository.searchEmployee(keyword);
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        employeeRepository.updateEmployee(employee.getEmail(), employee.getAddress(), employee.getId());
        return employee;
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
