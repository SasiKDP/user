package com.dataquadinc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data

public class LeaveCalender {
    @Id
    private String userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int noOfDays;
    private String leaveType;
    private Set<String> managerEmail;
    private String description;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public Set<String> getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(Set<String> managerEmail) {
        this.managerEmail = managerEmail;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
