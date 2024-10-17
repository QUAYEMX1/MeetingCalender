package com.example.Meeting_calender.Repository;

import com.example.Meeting_calender.Models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,String> {

}
