package yaremax.com.sa_task_04_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yaremax.com.sa_task_04_06.entity.Company;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.CompanyRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReferenceService {
    private final CompanyRepository companyRepository;

    public Company getExistingCompanyReference(UUID id) {
        companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " not found"));
        return companyRepository.getReferenceById(id);
    }
}
