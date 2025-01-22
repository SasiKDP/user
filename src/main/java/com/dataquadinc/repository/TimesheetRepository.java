package com.dataquadinc.repository;



import com.dataquadinc.model.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    Timesheet findByEmployeeIdAndClockOutIsNull(String employeeId);

    Timesheet findByEmployeeIdAndDate(String employeeId, String date);
    List<Timesheet> findByEmployeeId(String employeeId);


}
