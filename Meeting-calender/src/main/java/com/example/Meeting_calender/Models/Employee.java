package com.example.Meeting_calender.Models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {

    @Id
    String id; // Employee ID
    String name;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    List<Meeting> meetings;

}
