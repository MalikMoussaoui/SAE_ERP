package com.unilim.erp.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class McccDto {
    private UUID id;
    private String department;
    private String ue;
    private String years;
    private String semester;
    private Map<String, Object> form;
    private List<Map<String, Object>> ressourcesRows;
    private LocalDateTime savedAt;
}