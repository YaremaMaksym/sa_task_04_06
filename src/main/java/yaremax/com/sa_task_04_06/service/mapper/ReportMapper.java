package yaremax.com.sa_task_04_06.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import yaremax.com.sa_task_04_06.dto.ReportDto;
import yaremax.com.sa_task_04_06.entity.Report;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * This interface is used to map between {@link Report} entities and {@link ReportDto}s.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Mapper
public interface ReportMapper {
    /**
     * Singleton instance of the ReportMapper.
     */
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    /**
     * Converts a {@link ReportDto} to a {@link Report} entity.
     *
     * @param  dto  the {@link ReportDto} to convert
     * @return      the converted {@link Report} entity
     */
    Report toEntity(ReportDto dto);

    /**
     * Converts a {@link Report} entity to a {@link ReportDto}.
     *
     * @param  entity  the {@link Report} entity to convert
     * @return         the converted {@link ReportDto}
     */
    @Mapping(source = "company.id", target = "companyId")
    ReportDto toDto(Report entity);

    /**
     * Converts a list of {@link Report} entities to a list of {@link ReportDto}s.
     *
     * @param  entityList  the list of {@link Report} entities to convert
     * @return             the converted list of {@link ReportDto}s
     */
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    List<ReportDto> toDtoList(List<Report> entityList);
}
