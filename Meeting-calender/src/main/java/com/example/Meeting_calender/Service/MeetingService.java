package com.example.Meeting_calender.Service;

import com.example.Meeting_calender.Exceptions.EmployeeNotFoundException;
import com.example.Meeting_calender.Models.Employee;
import com.example.Meeting_calender.Models.Meeting;
import com.example.Meeting_calender.Models.TimeSlot;
import com.example.Meeting_calender.Repository.EmployeeRepository;
import com.example.Meeting_calender.Repository.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class MeetingService {
    @Autowired
    MeetingRepository meetingRepository;

    @Autowired
    EmployeeRepository employeeRepository;
    public Meeting bookMeeting(Meeting meeting) {
        return meetingRepository.save(meeting);
    }

    public List<TimeSlot> findFreeSlots(String employeeId1, String employeeId2, Duration duration) {
        // Fetch employees and throw custom exception if not found
        Employee employee1 = employeeRepository.findById(employeeId1)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId1 + " not found"));
        Employee employee2 = employeeRepository.findById(employeeId2)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + employeeId2 + " not found"));

        // Fetch meetings for both employees
        List<Meeting> meetings1 = meetingRepository.findByEmployee(employee1);
        List<Meeting> meetings2 = meetingRepository.findByEmployee(employee2);

        // Combine meetings from both employees and sort them by start time
        List<Meeting> allMeetings = new ArrayList<>(meetings1);
        allMeetings.addAll(meetings2);
        allMeetings.sort(Comparator.comparing(Meeting::getStartTime));

        // Define the working day start and end times
        LocalDateTime startOfDay = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(17).withMinute(0).withSecond(0).withNano(0);

        // Variable to keep track of the current time slot being checked
        LocalDateTime currentTime = startOfDay;
        List<TimeSlot> freeSlots = new ArrayList<>();

        for (Meeting meeting : allMeetings) {
            // Check if there's a free slot between the current time and the meeting start
            if (meeting.getStartTime().isAfter(currentTime) && meeting.getStartTime().isBefore(endOfDay)) {
                LocalDateTime freeSlotEnd = meeting.getStartTime();

                // Log meeting info for debugging
                System.out.println("Meeting starts at: " + meeting.getStartTime() + ", ends at: " + meeting.getEndTime());

                // Ensure the free slot is long enough to fit the required duration
                if (Duration.between(currentTime, freeSlotEnd).compareTo(duration) >= 0) {
                    System.out.println("Adding free slot from: " + currentTime + " to: " + freeSlotEnd);
                    freeSlots.add(new TimeSlot(currentTime, freeSlotEnd));
                }
            }

            // Update the current time to the end of the current meeting
            if (meeting.getEndTime().isAfter(currentTime)) {
                currentTime = meeting.getEndTime();
            }

            // If the current time exceeds the end of the working day, stop checking
            if (currentTime.isAfter(endOfDay)) {
                break;
            }
        }

        // Check if there's a free slot after the last meeting until the end of the day
        if (currentTime.isBefore(endOfDay) && Duration.between(currentTime, endOfDay).compareTo(duration) >= 0) {
            System.out.println("Adding final free slot from: " + currentTime + " to: " + endOfDay);
            freeSlots.add(new TimeSlot(currentTime, endOfDay));
        }

        // Return the free slots
        return freeSlots;
    }


    public List<String> findMeetingConflicts(Meeting newMeeting) {
        Employee employee = newMeeting.getEmployee();
        List<Meeting> meetings = meetingRepository.findByEmployee(employee);
        List<String> conflicts = new ArrayList<>();

        for (Meeting meeting : meetings) {
            if ((newMeeting.getStartTime().isBefore(meeting.getEndTime())) &&
                    (newMeeting.getEndTime().isAfter(meeting.getStartTime()))) {
                conflicts.add(meeting.getEmployee().getId());
            }
        }

        return conflicts;
    }
}
