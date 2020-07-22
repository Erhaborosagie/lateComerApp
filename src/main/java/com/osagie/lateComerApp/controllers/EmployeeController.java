package com.osagie.lateComerApp.controllers;

import com.osagie.lateComerApp.dto.EmployeeDto;
import com.osagie.lateComerApp.model.ArrivalTime;
import com.osagie.lateComerApp.model.Employee;
import com.osagie.lateComerApp.repository.ArrivalTimeRepository;
import com.osagie.lateComerApp.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v1/employee")
public class EmployeeController {

    private EmployeeService employeeService;
    private ArrivalTimeRepository arrivalTimeRepository;

    private LocalDateTime localTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(8,0,0));

    @Autowired
    public EmployeeController(EmployeeService employeeService, ArrivalTimeRepository arrivalTimeRepository) {
        this.employeeService = employeeService;
        this.arrivalTimeRepository = arrivalTimeRepository;
    }

    @PostMapping
    public ResponseEntity<Object> createEmployee(@Valid @RequestBody EmployeeDto employeeDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    Objects.requireNonNull(bindingResult.getFieldError()).getField() + " " +
                            Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        Employee employee;
        employee = employeeService.addEmployee(employeeDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(employee.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<Object> allEmployees(){
        return ResponseEntity.ok(employeeService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getMerchant(@PathVariable("id") Long id) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

    @GetMapping("/sorted")
    public ResponseEntity<Page<Employee>> sortedEmployees(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "dir", required = false, defaultValue = "desc") String dir,
            @RequestParam(value = "keyword", required = false, defaultValue = "debt") String keyword) {
        return ResponseEntity.ok(employeeService.findAll(page, pageSize, keyword, dir));
    }

    @GetMapping("search")
    public ResponseEntity<List<Employee>> searchEmployees(
            @RequestParam String keyword) {
        return ResponseEntity.ok(employeeService.searchEmployee(keyword));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody EmployeeDto employeeDto, @PathVariable Long id){
        Employee employee = employeeService.findById(id);
        if (employee == null)
            throw new RuntimeException("No employee with id " + id);
        employee.setEmail(employeeDto.getEmail());
        employee.setName(employeeDto.getName());
        employee.setAddress(employeeDto.getAddress());

        return ResponseEntity.ok(employeeService.updateEmployee(employee));
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id){
        Employee employee = employeeService.findById(id);
        if (employee == null)
            throw new RuntimeException("No employee with id " + id);
        employeeService.deleteEmployee(id);
    }

    @PostMapping("/{id}/clockin")
    public ResponseEntity<?> clockIn(@PathVariable Long id){

        Employee employee = employeeService.findById(id);
        if (employee == null)
            throw new RuntimeException("No employee with id " + id);

        LocalDateTime myObj = LocalDateTime.now();

        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setClockingTime(myObj);
        arrivalTime.setEmployees(employee);

        LocalDate localDate = myObj.toLocalDate();

        ArrivalTime latestArrivalTime = arrivalTimeRepository.findTopByEmployeesIdOrderByClockingTimeAsc(id).orElse(null);

        //Check that user is not new
        if (latestArrivalTime != null){

            //Check that user has not clocked in for the day
            if (localDate.toString().equals(latestArrivalTime.getClockingTime().toLocalDate().toString()))
                throw new RuntimeException("You've clocked in already");
            else {
                System.out.println(localDate);
                System.out.println(latestArrivalTime.getClockingTime().toLocalDate());
            }


        }

        long mins = ChronoUnit.MINUTES.between(localTime, myObj);
        if (mins>0){
            employee.setDebt(employee.getDebt() + (mins * 0.2));
        }
        arrivalTimeRepository.save(arrivalTime);
        return ResponseEntity.ok(employee);
    }
}
