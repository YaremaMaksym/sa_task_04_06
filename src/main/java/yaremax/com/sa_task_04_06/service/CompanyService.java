package yaremax.com.sa_task_04_06.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yaremax.com.sa_task_04_06.dto.CompanyDto;
import yaremax.com.sa_task_04_06.entity.Company;
import yaremax.com.sa_task_04_06.exception.custom.DuplicateResourceException;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.CompanyRepository;
import yaremax.com.sa_task_04_06.service.mapper.CompanyMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


/**
 * Service class for {@link Company} operations.
 * 
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

    /**
     * Creates a new {@link Company} with the given {@link CompanyDto}.
     * 
     * @param  createCompanyRequestDto   the {@link CompanyDto} with {@link Company} data
     * @return                           the created {@link CompanyDto}
     * @throws DuplicateResourceException if the {@link Company} with the given registration number already exists
     */
    @Transactional
    public CompanyDto createCompany(CompanyDto createCompanyRequestDto) {
        if (companyRepository.existsByRegistrationNumber(createCompanyRequestDto.getRegistrationNumber())) {
            throw new DuplicateResourceException("Company with registration number " + createCompanyRequestDto.getRegistrationNumber() + " already exists");
        }
        Company company = companyMapper.toEntity(createCompanyRequestDto);
        company.setCreated_at(LocalDate.now());
        Company savedCompany = companyRepository.save(company);
        return companyMapper.toDto(savedCompany);
    }

    /**
     * Gets all {@link Company}s.
     * 
     * @return list of {@link CompanyDto}s
     */
    public List<CompanyDto> getAllCompanies() {
        return companyMapper.toDtoList(companyRepository.findAll());
    }

    /**
     * Gets a {@link Company} by its ID.
     * 
     * @param  id   the ID of the {@link Company}
     * @return      the {@link CompanyDto} with the given ID
     * @throws ResourceNotFoundException if the {@link Company} with the given ID is not found
     */
    public CompanyDto getCompanyById(UUID id) {
        Company foundCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " not found"));
        return companyMapper.toDto(foundCompany);
    }

    /**
     * Updates a {@link Company} with the given {@link CompanyDto}.
     * 
     * @param  id                       the ID of the {@link Company} to update
     * @param  updateCompanyRequestDto  the {@link CompanyDto} with updated {@link Company} data
     * @return                          the updated {@link CompanyDto}
     * @throws ResourceNotFoundException if the {@link Company} with the given ID is not found
     * @throws DuplicateResourceException if the {@link Company} with the updated registration number already exists
     */
    @Transactional
    public CompanyDto updateCompany(UUID id, CompanyDto updateCompanyRequestDto) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " not found"));
        
        if (!updateCompanyRequestDto.getRegistrationNumber().equals(existingCompany.getRegistrationNumber())
            && companyRepository.existsByRegistrationNumber(updateCompanyRequestDto.getRegistrationNumber())) {
            throw new DuplicateResourceException("Company with registration number " + updateCompanyRequestDto.getRegistrationNumber() + " already exists");
        }

        Company company = companyMapper.toEntity(updateCompanyRequestDto);
        Company savedCompany = companyRepository.save(company);
        return companyMapper.toDto(savedCompany);
    }

    /**
     * Deletes a {@link Company} by its ID.
     * 
     * @param  id   the ID of the {@link Company} to delete
     * @throws ResourceNotFoundException if the {@link Company} with the given ID is not found
     */
    @Transactional
    public void deleteCompany(UUID id) {
        Company companyToDelete = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " not found"));
        companyRepository.delete(companyToDelete);
    }
}
