package com.example.Meeting_calender;

import com.example.Meeting_calender.Models.Employee;
import com.example.Meeting_calender.Models.Meeting;
import com.example.Meeting_calender.Models.TimeSlot;
import com.example.Meeting_calender.Repository.EmployeeRepository;
import com.example.Meeting_calender.Repository.MeetingRepository;
import com.example.Meeting_calender.Service.MeetingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class MeetingServiceTests {
    @InjectMocks
    private MeetingService meetingService;

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test booking a meeting
    @Test
    void testBookMeeting() {
        // Arrange
        Employee employee = new Employee("1", "John Doe", new ArrayList<>());
        Meeting meeting = new Meeting(null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), employee);
        when(meetingRepository.save(meeting)).thenReturn(meeting);

        // Act
        Meeting savedMeeting = meetingService.bookMeeting(meeting);

        // Assert
        Assertions.assertNotNull(savedMeeting);
        Assertions.assertEquals(employee, savedMeeting.getEmployee());
        verify(meetingRepository, times(1)).save(meeting);
    }

    // Test finding free slots
    @Test
    void testFindFreeSlots() {
        // Arrange
        Employee employee1 = new Employee("1", "John Doe", new ArrayList<>());
        Employee employee2 = new Employee("2", "Jane Doe", new ArrayList<>());

        List<Meeting> employee1Meetings = new ArrayList<>();
        employee1Meetings.add(new Meeting(null, LocalDateTime.of(2024, 10, 17, 9, 0), LocalDateTime.of(2024, 10, 17, 10, 0), employee1));
        employee1Meetings.add(new Meeting(null, LocalDateTime.of(2024, 10, 17, 14, 0), LocalDateTime.of(2024, 10, 17, 15, 0), employee1));

        List<Meeting> employee2Meetings = new ArrayList<>();
        employee2Meetings.add(new Meeting(null, LocalDateTime.of(2024, 10, 17, 11, 0), LocalDateTime.of(2024, 10, 17, 12, 0), employee2));

        when(employeeRepository.findById("1")).thenReturn(Optional.of(employee1));
        when(employeeRepository.findById("2")).thenReturn(Optional.of(employee2));
        when(meetingRepository.findByEmployee(employee1)).thenReturn(employee1Meetings);
        when(meetingRepository.findByEmployee(employee2)).thenReturn(employee2Meetings);

        // Act
        List<TimeSlot> freeSlots = meetingService.findFreeSlots("1", "2", Duration.ofMinutes(30));

        // Assert
        Assertions.assertFalse(freeSlots.isEmpty());
        Assertions.assertEquals(3, freeSlots.size());
        Assertions.assertEquals(LocalDateTime.of(2024, 10, 17, 10, 0), freeSlots.get(0).getStartTime());
        Assertions.assertEquals(LocalDateTime.of(2024, 10, 17, 11, 0), freeSlots.get(0).getEndTime());
    }

    // Test finding meeting conflicts
    @Test
    void testFindMeetingConflicts() {
        // Arrange
        Employee employee = new Employee("1", "John Doe", new ArrayList<>());
        List<Meeting> existingMeetings = new ArrayList<>();
        existingMeetings.add(new Meeting(null, LocalDateTime.of(2024, 10, 17, 9, 0), LocalDateTime.of(2024, 10, 17, 10, 0), employee));

        Meeting newMeeting = new Meeting(null, LocalDateTime.of(2024, 10, 17, 9, 30), LocalDateTime.of(2024, 10, 17, 10, 30), employee);

        when(meetingRepository.findByEmployee(employee)).thenReturn(existingMeetings);

        // Act
        List<String> conflicts = meetingService.findMeetingConflicts(newMeeting);

        // Assert
        Assertions.assertFalse(conflicts.isEmpty());
        Assertions.assertEquals(1, conflicts.size());
        Assertions.assertEquals("1", conflicts.get(0));
    }
}
