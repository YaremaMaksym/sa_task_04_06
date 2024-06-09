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

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper = CompanyMapper.INSTANCE;

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

    public List<CompanyDto> getAllCompanies() {
        return companyMapper.toDtoList(companyRepository.findAll());
    }

    public CompanyDto getCompanyById(UUID id) {
        Company foundCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company with id " + id + " not found"));
        return companyMapper.toDto(foundCompany);
    }

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

    @Transactional
    public void deleteCompany(UUID id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company with id " + id + " not found");
        }
        companyRepository.deleteById(id);
    }
}
