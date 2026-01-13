package com.unilim.erp.dto;

import com.unilim.erp.domain.UserRole;
import com.unilim.erp.domain.UserStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class AppUserDto {
    private UUID id;
    private String email;
    private String displayName;
    private String phone;
    private UserRole role;
    private UserStatus status;

    private String poste;
    private String department;
}
