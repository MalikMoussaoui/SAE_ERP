package com.unilim.erp.dto;

import com.unilim.erp.domain.UserRole;

public record RegistrationRequest(
        String email,
        String password,
        String displayName,
        String phone,
        UserRole role
) { }
