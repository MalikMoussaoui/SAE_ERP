package com.unilim.erp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CompetencyNotFoundException extends RuntimeException {
    public CompetencyNotFoundException(UUID id) {
        super("Compétence non trouvée avec l'ID : " + id);
    }
}