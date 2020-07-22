package com.osagie.lateComerApp.dto;

import com.osagie.lateComerApp.model.Employee;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class EmployeeDto {
    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String address;

    public Employee toEmployee(){
        Employee employee = new Employee();
        employee.setAddress(address);
        employee.setName(name);
        employee.setEmail(email);
        employee.setDebt(0.0);
        return employee;
    }
}
