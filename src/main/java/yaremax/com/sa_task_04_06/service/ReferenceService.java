package yaremax.com.sa_task_04_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yaremax.com.sa_task_04_06.entity.Company;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.CompanyRepository;

import java.util.UUID;


/**
 * This service provides methods for obtaining references to entities.
 * It is used to ensure that only valid references are used in the application.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Service
@RequiredArgsConstructor
public class ReferenceService {
    private final CompanyRepository companyRepository;

    /**
     * Returns an existing {@link Company} reference.
     *
     * @param  id  the ID of the {@link Company}
     * @return     the existing {@link Company} reference
     * @throws ResourceNotFoundException if the {@link Company} with the given ID is not found
     */
    public Company getExistingCompanyReference(UUID id) {
        companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " not found"));
        return companyRepository.getReferenceById(id);
    }
}
