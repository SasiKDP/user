package com.dataquadinc.dto;

import com.dataquadinc.model.Roles;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class EmployeeWithRole
{
    @JsonProperty("employeeId")
    private String employeeId;
    @JsonProperty("employeeName")
    private String employeeName;
    @JsonProperty("roles")
    private String Roles;
    @JsonProperty("email")
    private String email;


    public EmployeeWithRole(String employeeId, String employeeName, String roles,String email) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.Roles = roles;
        this.email=email;
    }


}
