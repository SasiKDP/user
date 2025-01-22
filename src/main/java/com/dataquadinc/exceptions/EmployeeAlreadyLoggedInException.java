package com.dataquadinc.exceptions;

public class EmployeeAlreadyLoggedInException extends RuntimeException {

    private String employeeId;

    public EmployeeAlreadyLoggedInException(String employeeId) {
        super("Employee " + employeeId + " has already logged in today.");
        this.employeeId = employeeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }
}
