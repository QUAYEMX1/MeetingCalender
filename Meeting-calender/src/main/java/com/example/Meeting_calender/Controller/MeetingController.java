package com.example.Meeting_calender.Controller;

import com.example.Meeting_calender.Models.Meeting;
import com.example.Meeting_calender.Models.TimeSlot;
import com.example.Meeting_calender.Service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    @Autowired
    MeetingService meetingService;

    @PostMapping("/book")
    public ResponseEntity<Meeting> bookMeeting(@RequestBody Meeting meeting) throws Exception{
        Meeting savedMeeting = meetingService.bookMeeting(meeting);
        return new ResponseEntity<>(savedMeeting,HttpStatus.OK);
    }

    @GetMapping("/free-slots/{employeeId1}/{employeeId2}/{durationInMinutes}")
    public ResponseEntity<List<TimeSlot>> findFreeSlots(@PathVariable String employeeId1,
                                                        @PathVariable String employeeId2,
                                                        @PathVariable int durationInMinutes) throws Exception{
        Duration duration = Duration.ofMinutes(durationInMinutes);
        List<TimeSlot> freeSlots = meetingService.findFreeSlots(employeeId1, employeeId2, duration);
        return new ResponseEntity<>(freeSlots,HttpStatus.OK);
    }

    @PostMapping("/conflicts")
    public ResponseEntity<List<String>> findMeetingConflicts(@RequestBody Meeting newMeeting) throws Exception{
        List<String> conflicts = meetingService.findMeetingConflicts(newMeeting);
        return new ResponseEntity<>(conflicts,HttpStatus.OK);
    }
}
