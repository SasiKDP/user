package com.dataquadinc.dto;

import com.dataquadinc.model.Roles;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @JsonProperty("designation")
    private String designation;
    @JsonProperty("joiningDate")
    private LocalDate joiningDate;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("dob")
    private String dob;
    @JsonProperty("phoneNumber")
    private String phoneNumber;
    @JsonProperty("personalemail")
    private String personalemail;
    @JsonProperty("status")
    private String status;


//    public EmployeeWithRole(String employeeId, String employeeName, String roles,String email) {
//        this.employeeId = employeeId;
//        this.employeeName = employeeName;
//        this.Roles = roles;
//        this.email=email;
//    }

    public EmployeeWithRole(String employeeId, String employeeName, String roles, String email, String designation, LocalDate joiningDate, String gender, String dob, String phoneNumber, String personalemail,String status) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        Roles = roles;
        this.email = email;
        this.designation = designation;
        this.joiningDate = joiningDate;
        this.gender = gender;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.personalemail = personalemail;
        this.status=status;
    }
}
