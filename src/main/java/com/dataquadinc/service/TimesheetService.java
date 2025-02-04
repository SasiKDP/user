package com.dataquadinc.service;


import com.dataquadinc.exceptions.EmployeeAlreadyLoggedInException;
import com.dataquadinc.model.Timesheet_prod;
import com.dataquadinc.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class TimesheetService {

    @Autowired
    private TimesheetRepository timesheetRepository;

    public Timesheet_prod logIn(String employeeId) {
        Timesheet_prod existingTimesheet = timesheetRepository.findByEmployeeIdAndDate(employeeId, LocalDate.now().toString());
        if (existingTimesheet != null) {
            throw new EmployeeAlreadyLoggedInException(employeeId);
        }

        Timesheet_prod timesheet = new Timesheet_prod();
        timesheet.setEmployeeId(employeeId);
        LocalTime clockInTime = LocalTime.now();
        timesheet.setClockIn(formatTime(clockInTime));
        timesheet.setDate(LocalDate.now().toString());
        timesheet.setStatus("Present");
        timesheet.setLate(calculateLate(timesheet.getClockIn()));

        return timesheetRepository.save(timesheet);
    }

    public Timesheet_prod logOut(String employeeId) {
        Timesheet_prod timesheet = timesheetRepository.findByEmployeeIdAndClockOutIsNull(employeeId);

        if (timesheet == null) {
            throw new RuntimeException("No active session found for employee: " + employeeId);
        }

        LocalTime clockOutTime = LocalTime.now();
        timesheet.setClockOut(formatTime(clockOutTime));
        timesheet.setEarlyLeaving(calculateEarlyLeaving(timesheet.getClockOut()));
        timesheet.setOvertime(calculateOvertime(timesheet.getClockIn(), timesheet.getClockOut()));

        return timesheetRepository.save(timesheet);
    }

    public List<Timesheet_prod> getAllTimesheets() {
        return timesheetRepository.findAll();
    }

    private String formatTime(LocalTime time) {
        return time.toString(); // Default format (HH:mm)
    }

    private String calculateLate(String clockInTime) {
        LocalTime expectedClockIn = LocalTime.of(9, 0); // 9:00 AM
        LocalTime clockIn = LocalTime.parse(clockInTime);

        if (clockIn.isAfter(expectedClockIn)) {
            long minutesLate = ChronoUnit.MINUTES.between(expectedClockIn, clockIn);
            return String.format("%02d:%02d", minutesLate / 60, minutesLate % 60);
        }
        return "00:00";
    }

    private String calculateEarlyLeaving(String clockOutTime) {
        LocalTime expectedClockOut = LocalTime.of(18, 0); // 6:00 PM
        LocalTime clockOut = LocalTime.parse(clockOutTime);

        if (clockOut.isBefore(expectedClockOut)) {
            long minutesEarly = ChronoUnit.MINUTES.between(clockOut, expectedClockOut);
            return String.format("-%02d:%02d", minutesEarly / 60, minutesEarly % 60);
        }
        return "00:00";
    }

    private String calculateOvertime(String clockInTime, String clockOutTime) {
        LocalTime expectedClockOut = LocalTime.of(18, 0); // 6:00 PM
        LocalTime clockOut = LocalTime.parse(clockOutTime);

        if (clockOut.isAfter(expectedClockOut)) {
            long minutesOver = ChronoUnit.MINUTES.between(expectedClockOut, clockOut);
            return String.format("%02d:%02d", minutesOver / 60, minutesOver % 60);
        }
        return "00:00";
    }
    public List<Timesheet_prod> getTimesheetsByEmployeeId(String employeeId) {
        return timesheetRepository.findByEmployeeId(employeeId);
    }

}
