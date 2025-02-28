package com.dataquadinc.repository;

import com.dataquadinc.model.LeaveCalender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface leaveCalendarDao extends JpaRepository<LeaveCalender,String> {
}
