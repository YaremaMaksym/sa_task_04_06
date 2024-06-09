package yaremax.com.sa_task_04_06.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import yaremax.com.sa_task_04_06.dto.ReportDetailsDto;
import yaremax.com.sa_task_04_06.entity.ReportDetails;

import java.util.List;

@Mapper
public interface ReportDetailsMapper {
    ReportDetailsMapper INSTANCE = Mappers.getMapper(ReportDetailsMapper.class);

    ReportDetails toEntity(ReportDetailsDto dto);
    ReportDetailsDto toDto(ReportDetails entity);
    List<ReportDetailsDto> toDtoList(List<ReportDetails> entityList);
}
