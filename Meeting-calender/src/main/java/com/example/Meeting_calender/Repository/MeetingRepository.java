package com.example.Meeting_calender.Repository;

import com.example.Meeting_calender.Models.Employee;
import com.example.Meeting_calender.Models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {

    List<Meeting> findByEmployee(Employee employee1);

}
