package com.dataquadinc.dto;

import com.dataquadinc.model.Roles_prod;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class RoleDto {
    private String roleName;

    // Constructor to initialize RoleDto from Roles entity
    public RoleDto(Roles_prod role) {
        this.roleName = role.getName().name();  // Assuming Roles is an enum
    }
}
