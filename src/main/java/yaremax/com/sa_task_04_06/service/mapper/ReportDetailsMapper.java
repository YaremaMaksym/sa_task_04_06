package yaremax.com.sa_task_04_06.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import yaremax.com.sa_task_04_06.dto.ReportDetailsDto;
import yaremax.com.sa_task_04_06.entity.ReportDetails;

import java.util.List;

/**
 * This interface is used to map between {@link ReportDetails} entities and {@link ReportDetailsDto}s.
 *
 * @author Yaremax
 * @version 1.0
 * @since 2024-10-06
 */
@Mapper
public interface ReportDetailsMapper {
    /**
     * Singleton instance of the ReportDetailsMapper.
     */
    ReportDetailsMapper INSTANCE = Mappers.getMapper(ReportDetailsMapper.class);

    /**
     * Converts a {@link ReportDetailsDto} to a {@link ReportDetails} entity.
     *
     * @param  dto  the {@link ReportDetailsDto} to convert
     * @return      the converted {@link ReportDetails} entity
     */
    ReportDetails toEntity(ReportDetailsDto dto);

    /**
     * Converts a {@link ReportDetails} entity to a {@link ReportDetailsDto}.
     *
     * @param  entity  the {@link ReportDetails} entity to convert
     * @return         the converted {@link ReportDetailsDto}
     */
    ReportDetailsDto toDto(ReportDetails entity);

    /**
     * Converts a list of {@link ReportDetails} entities to a list of {@link ReportDetailsDto}s.
     *
     * @param  entityList  the list of {@link ReportDetails} entities to convert
     * @return             the converted list of {@link ReportDetailsDto}s
     */
    List<ReportDetailsDto> toDtoList(List<ReportDetails> entityList);
}
