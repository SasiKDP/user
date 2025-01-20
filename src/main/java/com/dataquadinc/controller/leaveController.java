package com.dataquadinc.controller;

import com.dataquadinc.dto.leaveCalenderDto;
import com.dataquadinc.model.LeaveCalender;
import com.dataquadinc.service.leaveCalenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class leaveController {
    @Autowired
    private leaveCalenderService leaveCalendarService;

    @PostMapping("/save")
    public ResponseEntity<LeaveCalender> saveLeave(@RequestBody leaveCalenderDto dto) {
        LeaveCalender savedLeave = leaveCalendarService.saveLeave(dto);
        return ResponseEntity.ok(savedLeave);
    }
}
