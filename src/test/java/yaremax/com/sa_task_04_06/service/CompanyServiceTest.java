package yaremax.com.sa_task_04_06.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yaremax.com.sa_task_04_06.dto.CompanyDto;
import yaremax.com.sa_task_04_06.entity.Company;
import yaremax.com.sa_task_04_06.exception.custom.DuplicateResourceException;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.CompanyRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    private CompanyService companyService;

    @BeforeEach
    void setUp() {
        companyService = new CompanyService(companyRepository);
    }

    @Nested
    class AddCompanyTests {
        @Test
        void addCompany_shouldAddCompany_whenRegistrationNumberNotExists() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            CompanyDto companyDto = CompanyDto.builder()
                    .id(companyId)
                    .name("Test Company")
                    .registrationNumber("REG123")
                    .address("123 Test St")
                    .created_at(LocalDate.now())
                    .build();

            Company company = Company.builder()
                    .id(companyId)
                    .name("Test Company")
                    .registrationNumber("REG123")
                    .address("123 Test St")
                    .created_at(LocalDate.now())
                    .build();

            when(companyRepository.existsByRegistrationNumber(companyDto.getRegistrationNumber())).thenReturn(false);
            when(companyRepository.save(any(Company.class))).thenReturn(company);

            // Act
            CompanyDto savedCompanyDto = companyService.addCompany(companyDto);

            // Assert
            assertThat(savedCompanyDto).isEqualTo(companyDto);
            verify(companyRepository).existsByRegistrationNumber(companyDto.getRegistrationNumber());
            verify(companyRepository).save(any(Company.class));
        }

        @Test
        void addCompany_shouldThrowDuplicateResourceException_whenRegistrationNumberExists() {
            // Arrange
            CompanyDto companyDto = CompanyDto.builder()
                    .registrationNumber("REG123")
                    .build();

            when(companyRepository.existsByRegistrationNumber(companyDto.getRegistrationNumber())).thenReturn(true);

            // Act & Assert
            assertThatExceptionOfType(DuplicateResourceException.class)
                    .isThrownBy(() -> companyService.addCompany(companyDto))
                    .withMessageContaining(companyDto.getRegistrationNumber());

            verify(companyRepository).existsByRegistrationNumber(companyDto.getRegistrationNumber());
            verify(companyRepository, never()).save(any(Company.class));
        }
    }

    @Nested
    class GetAllCompaniesTests {
        @Test
        void getAllCompanies_shouldReturnListOfCompanyDtos() {
            // Arrange
            List<Company> companies = Arrays.asList(
                    Company.builder().id(UUID.randomUUID()).name("Company1").build(),
                    Company.builder().id(UUID.randomUUID()).name("Company2").build()
            );
            when(companyRepository.findAll()).thenReturn(companies);

            // Act
            List<CompanyDto> companyDtos = companyService.getAllCompanies();

            // Assert
            assertThat(companyDtos).hasSize(2);
            assertThat(companyDtos.get(0).getName()).isEqualTo("Company1");
            assertThat(companyDtos.get(1).getName()).isEqualTo("Company2");
            verify(companyRepository).findAll();
        }
    }

    @Nested
    class GetCompanyByIdTests {
        @Test
        void getCompanyById_shouldReturnCompanyDto_whenCompanyExists() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            Company company = Company.builder()
                    .id(companyId)
                    .name("Test Company")
                    .build();
            when(companyRepository.findById(companyId)).thenReturn(Optional.of(company));

            // Act
            CompanyDto companyDto = companyService.getCompanyById(companyId);

            // Assert
            assertThat(companyDto.getId()).isEqualTo(companyId);
            assertThat(companyDto.getName()).isEqualTo("Test Company");
            verify(companyRepository).findById(companyId);
        }

        @Test
        void getCompanyById_shouldThrowResourceNotFoundException_whenCompanyNotFound() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> companyService.getCompanyById(companyId))
                    .withMessageContaining(companyId.toString());

            verify(companyRepository).findById(companyId);
        }
    }

    @Nested
    class UpdateCompanyTests {
        @Test
        void updateCompany_shouldUpdateCompany_whenCompanyExistsAndRegistrationNumberUnchanged() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            Company existingCompany = Company.builder()
                    .id(companyId)
                    .name("Old Company")
                    .registrationNumber("REG123")
                    .build();
            CompanyDto updateDto = CompanyDto.builder()
                    .name("New Company")
                    .registrationNumber("REG123")
                    .build();

            when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
            when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            CompanyDto updatedCompanyDto = companyService.updateCompany(companyId, updateDto);

            // Assert
            assertThat(updatedCompanyDto.getName()).isEqualTo("New Company");
            assertThat(updatedCompanyDto.getRegistrationNumber()).isEqualTo("REG123");
            verify(companyRepository).findById(companyId);
            verify(companyRepository).save(any(Company.class));
        }

        @Test
        void updateCompany_shouldUpdateCompany_whenCompanyExistsAndNewRegistrationNumberNotExists() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            Company existingCompany = Company.builder()
                    .id(companyId)
                    .registrationNumber("REG123")
                    .build();
            CompanyDto updateDto = CompanyDto.builder()
                    .registrationNumber("REG456")
                    .build();

            when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
            when(companyRepository.existsByRegistrationNumber("REG456")).thenReturn(false);
            when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // Act
            CompanyDto updatedCompanyDto = companyService.updateCompany(companyId, updateDto);

            // Assert
            assertThat(updatedCompanyDto.getRegistrationNumber()).isEqualTo("REG456");
            verify(companyRepository).findById(companyId);
            verify(companyRepository).existsByRegistrationNumber("REG456");
            verify(companyRepository).save(any(Company.class));
        }

        @Test
        void updateCompany_shouldThrowResourceNotFoundException_whenCompanyNotFound() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            CompanyDto updateDto = CompanyDto.builder().build();

            when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> companyService.updateCompany(companyId, updateDto))
                    .withMessageContaining(companyId.toString());

            verify(companyRepository).findById(companyId);
            verify(companyRepository, never()).existsByRegistrationNumber(anyString());
            verify(companyRepository, never()).save(any(Company.class));
        }

        @Test
        void updateCompany_shouldThrowDuplicateResourceException_whenNewRegistrationNumberExists() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            Company existingCompany = Company.builder()
                    .id(companyId)
                    .registrationNumber("REG123")
                    .build();
            CompanyDto updateDto = CompanyDto.builder()
                    .registrationNumber("REG456")
                    .build();

            when(companyRepository.findById(companyId)).thenReturn(Optional.of(existingCompany));
            when(companyRepository.existsByRegistrationNumber("REG456")).thenReturn(true);

            // Act & Assert
            assertThatExceptionOfType(DuplicateResourceException.class)
                    .isThrownBy(() -> companyService.updateCompany(companyId, updateDto))
                    .withMessageContaining("REG456");

            verify(companyRepository).findById(companyId);
            verify(companyRepository).existsByRegistrationNumber("REG456");
            verify(companyRepository, never()).save(any(Company.class));
        }
    }

    @Nested
    class DeleteCompanyTests {
        @Test
        void deleteCompany_shouldDeleteCompany_whenCompanyExists() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            when(companyRepository.existsById(companyId)).thenReturn(true);
            doNothing().when(companyRepository).deleteById(companyId);

            // Act
            companyService.deleteCompany(companyId);

            // Assert
            verify(companyRepository).existsById(companyId);
            verify(companyRepository).deleteById(companyId);
        }

        @Test
        void deleteCompany_shouldThrowResourceNotFoundException_whenCompanyNotFound() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            when(companyRepository.existsById(companyId)).thenReturn(false);

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> companyService.deleteCompany(companyId))
                    .withMessageContaining(companyId.toString());

            verify(companyRepository).existsById(companyId);
            verify(companyRepository, never()).deleteById(any(UUID.class));
        }
    }
}