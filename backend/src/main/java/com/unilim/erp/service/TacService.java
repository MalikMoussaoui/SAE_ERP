package com.unilim.erp.service;

import com.unilim.erp.entities.Tac;
import com.unilim.erp.repositories.TacRepository; // CORRECTED: 'repositories' (plural) to match your folder structure
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TacService {

    private final TacRepository tacRepository;


    public List<Tac> getAllEntries() {
        return tacRepository.findAll();
    }

    public Tac getEntryById(UUID id) {
        return tacRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket TAC introuvable avec l'ID : " + id));
    }

    public Tac createEntry(Tac entry) {
        entry.setId(null);
        return tacRepository.save(entry);
    }

    public Tac updateEntry(UUID id, Tac entryDetails) {
        return tacRepository.findById(id)
                .map(existingEntry -> {
                    // Update modifiable fields
                    existingEntry.setObservation(entryDetails.getObservation());
                    existingEntry.setOrigin(entryDetails.getOrigin());
                    existingEntry.setThematic(entryDetails.getThematic());
                    existingEntry.setCorrection(entryDetails.getCorrection());
                    existingEntry.setAnalyseCauses(entryDetails.getAnalyseCauses());
                    existingEntry.setAction(entryDetails.getAction());
                    existingEntry.setDueDate(entryDetails.getDueDate());
                    existingEntry.setStatus(entryDetails.getStatus());
                    existingEntry.setCommentary(entryDetails.getCommentary());

                    return tacRepository.save(existingEntry);
                })
                .orElseThrow(() -> new RuntimeException("Ticket TAC introuvable avec l'ID : " + id));
    }

    public void deleteEntry(UUID id) {
        tacRepository.deleteById(id);
    }
}