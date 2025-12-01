package com.unilim.erp.service;

import com.unilim.erp.entities.ResourceSheet;
import com.unilim.erp.domain.ResourceSheetStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    @SpringBootTest
    class ResourceSheetServiceTest {

        @Autowired
        private ResourceSheetService resourceSheetService;

        @Test
        void createAndRetrieveResourceSheet() {
            ResourceSheet fiche = new ResourceSheet();
            fiche.setObjectives("Objectif");
            fiche.setPrerequisites("Prérequis");
            fiche.setModalities("CM");
            fiche.setHoursCm(10);
            fiche.setHoursTd(5);
            fiche.setHoursTp(0);

            ResourceSheet savedFiche = resourceSheetService.createResourceSheet(fiche);

            assertNotNull(savedFiche.getId(), "ID non null après sauvegarde");
            assertEquals("Objectif", savedFiche.getObjectives());
            assertEquals(ResourceSheetStatus.DRAFT, savedFiche.getStatus());

            List<ResourceSheet> allFiches = resourceSheetService.getAllResourceSheets();
            assertFalse(allFiches.isEmpty(), "liste des fiches non vide");
        }
    }

