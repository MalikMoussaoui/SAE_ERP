package com.unilim.erp.service;

import com.unilim.erp.entities.Ue;
import com.unilim.erp.repositories.UeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UeService {
    private final UeRepository ueRepository;

    public UeService(UeRepository ueRepository) {
        this.ueRepository = ueRepository;
    }

    public List<Ue> findAll() {
        return ueRepository.findAll();
    }

    public Ue createUe(Ue ue) {
        return ueRepository.save(ue);
    }
}
