package com.dataquadinc.service;

import com.dataquadinc.dto.leaveCalenderDto;
import com.dataquadinc.model.LeaveCalender;
import com.dataquadinc.repository.leaveCalendarDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class leaveCalenderService {

    @Autowired
    private leaveCalendarDao leaveCalendarDaoo;

    @Autowired
    private EmailService emailService;  // Inject the EmailService

    public LeaveCalender saveLeave(leaveCalenderDto dto) {
        LeaveCalender leave = new LeaveCalender();
        leave.setUserId(dto.getUserId());
        leave.setUserName(dto.getUserName());
        leave.setManagerId(dto.getManagerId());
        leave.setManagerEmail(dto.getManagerEmail());
        leave.setDescription(dto.getDescription());
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setNoOfDays(dto.getNoOfDays());
        leave.setLeaveType(dto.getLeaveType());


        // Save the leave
        LeaveCalender savedLeave = leaveCalendarDaoo.save(leave);

        // Send email to manager after leave is saved
        String managerEmail = dto.getManagerEmail(); // Assuming you have the manager's email in the DTO
        String subject = "Leave Request Notification";
        String body = "Respected Manager "  + ",\n\n" +
                "I am writing to formally request leave for the following period:\n\n" +
                "Start Date: " + dto.getStartDate() + "\n" +
                "End Date: " + dto.getEndDate() + "\n" +
                "Number of Days: " + dto.getNoOfDays() + "\n" +
                "Leave Type: " + dto.getLeaveType() + "\n\n" +
                "Reason for Leave:\n" +
                dto.getDescription() + "\n\n" +
                dto.getUserId() + "\n" +
                dto.getUserName();

        // Send the email
        emailService.sendEmail(managerEmail, subject, body);

        return savedLeave;
    }
}
