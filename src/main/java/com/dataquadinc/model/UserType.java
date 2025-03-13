package com.dataquadinc.model;

import jakarta.persistence.Table;

@Table(name = "UserType_prod")
public enum UserType {

    ADMIN,
    SUPERADMIN,
    EMPLOYEE,
    TEAMLEAD,
    BDM


}
