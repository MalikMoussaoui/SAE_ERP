package com.unilim.erp.service;

import com.unilim.erp.entities.Department;
import com.unilim.erp.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(UUID id) {
        return departmentRepository.findById(id);
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(UUID id, Department departmentDetails) {
        return departmentRepository.findById(id)
                .map(existing -> {
                    existing.setLabel(departmentDetails.getLabel());
                    return departmentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Département introuvable avec l'ID : " + id));
    }

    public void deleteDepartment(UUID id) {
        departmentRepository.deleteById(id);
    }
}
