package com.dataquadinc.dto;

import com.dataquadinc.model.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data

public class EmployeeWithRole
{
    private String employeeId;
    private String employeeName;
    private String Roles;


    public EmployeeWithRole(String employeeId, String employeeName, String roles) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        Roles = roles;
    }
}
