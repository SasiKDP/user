package com.dataquadinc.repository;

import com.dataquadinc.model.LeaveCalender_prod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface leaveCalendarDao extends JpaRepository<LeaveCalender_prod,String> {
}
