package yaremax.com.sa_task_04_06.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import yaremax.com.sa_task_04_06.dto.CompanyDto;
import yaremax.com.sa_task_04_06.entity.Company;

import java.util.List;

/**
 * This interface is used to map between {@link Company} entities and {@link CompanyDto}s.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Mapper
public interface CompanyMapper {
    /**
     * Singleton instance of the CompanyMapper.
     */
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    /**
     * Converts a {@link CompanyDto} to a {@link Company} entity.
     *
     * @param  dto  the {@link CompanyDto} to convert
     * @return      the converted {@link Company} entity
     */
    Company toEntity(CompanyDto dto);

    /**
     * Converts a {@link Company} entity to a {@link CompanyDto}.
     *
     * @param  company  the {@link Company} entity to convert
     * @return          the converted {@link CompanyDto}
     */
    CompanyDto toDto(Company company);

    /**
     * Converts a list of {@link Company} entities to a list of {@link CompanyDto}s.
     *
     * @param  entityList  the list of {@link Company} entities to convert
     * @return             the converted list of {@link CompanyDto}s
     */
    List<CompanyDto> toDtoList(List<Company> entityList);
}
