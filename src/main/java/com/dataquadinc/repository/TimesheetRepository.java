package com.dataquadinc.repository;



import com.dataquadinc.model.Timesheet_prod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimesheetRepository extends JpaRepository<Timesheet_prod, Long> {

    Timesheet_prod findByEmployeeIdAndClockOutIsNull(String employeeId);

    Timesheet_prod findByEmployeeIdAndDate(String employeeId, String date);
    List<Timesheet_prod> findByEmployeeId(String employeeId);


}
