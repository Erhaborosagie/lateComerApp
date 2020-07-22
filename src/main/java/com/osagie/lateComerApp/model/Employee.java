package com.osagie.lateComerApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Employee {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String address;

    @NotNull
    private Double debt;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employees")
    private List<ArrivalTime> arrivalTimes;
}
