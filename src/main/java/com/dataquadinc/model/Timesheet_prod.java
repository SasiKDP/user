package com.dataquadinc.model;

import jakarta.persistence.*;

@Entity
public class Timesheet_prod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String employeeId;

    private String clockIn;
    private String clockOut;
    private String date;
    private String late;
    private String earlyLeaving;
    private String overtime;
    private String status;

    // Default Constructor
    public Timesheet_prod() {
    }

    // Parameterized Constructor without logoutTime
    public Timesheet_prod(String employeeId, String clockIn, String clockOut, String date, String late,
                          String earlyLeaving, String overtime, String status) {
        this.employeeId = employeeId;
        this.clockIn = clockIn;
        this.clockOut = clockOut;
        this.date = date;
        this.late = late;
        this.earlyLeaving = earlyLeaving;
        this.overtime = overtime;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getClockIn() {
        return clockIn;
    }

    public void setClockIn(String clockIn) {
        this.clockIn = clockIn;
    }

    public String getClockOut() {
        return clockOut;
    }

    public void setClockOut(String clockOut) {
        this.clockOut = clockOut;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public String getEarlyLeaving() {
        return earlyLeaving;
    }

    public void setEarlyLeaving(String earlyLeaving) {
        this.earlyLeaving = earlyLeaving;
    }

    public String getOvertime() {
        return overtime;
    }

    public void setOvertime(String overtime) {
        this.overtime = overtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Timesheet{" +
                "id=" + id +
                ", employeeId='" + employeeId + '\'' +
                ", clockIn='" + clockIn + '\'' +
                ", clockOut='" + clockOut + '\'' +
                ", date='" + date + '\'' +
                ", late='" + late + '\'' +
                ", earlyLeaving='" + earlyLeaving + '\'' +
                ", overtime='" + overtime + '\'' +
                ", status='" + status +
                '}';
    }
}
