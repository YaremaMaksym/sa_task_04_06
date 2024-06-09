package yaremax.com.sa_task_04_06.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yaremax.com.sa_task_04_06.entity.Company;
import yaremax.com.sa_task_04_06.exception.custom.ResourceNotFoundException;
import yaremax.com.sa_task_04_06.repository.CompanyRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReferenceServiceTest {

    @Mock
    private CompanyRepository companyRepository;

    private ReferenceService referenceService;

    @BeforeEach
    void setUp() {
        referenceService = new ReferenceService(companyRepository);
    }

    @Nested
    class getExistingCompanyReferenceTests {
        @Test
        void getExistingCompanyReference_shouldReturnCompanyReference_whenCompanyExists() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            Company company = Company.builder().id(companyId).build();

            when(companyRepository.existsById(companyId)).thenReturn(true);
            when(companyRepository.getReferenceById(companyId)).thenReturn(company);

            // Act
            Company result = referenceService.getExistingCompanyReference(companyId);

            // Assert
            assertThat(result).isSameAs(company);
            verify(companyRepository).existsById(companyId);
            verify(companyRepository).getReferenceById(companyId);
        }

        @Test
        void getExistingCompanyReference_shouldThrowResourceNotFoundException_whenCompanyNotFound() {
            // Arrange
            UUID companyId = UUID.randomUUID();
            when(companyRepository.existsById(companyId)).thenReturn(false);

            // Act & Assert
            assertThatExceptionOfType(ResourceNotFoundException.class)
                    .isThrownBy(() -> referenceService.getExistingCompanyReference(companyId))
                    .withMessageContaining(companyId.toString());

            verify(companyRepository).existsById(companyId);
            verify(companyRepository, never()).getReferenceById(any(UUID.class));
        }
    }
}