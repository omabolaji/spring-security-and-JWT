package com.famousb.securityjwt.dto.request;

import lombok.Data;

@Data
public class AddRoleDto {
    private String email;
    private String roleName;
}
