package yaremax.com.sa_task_04_06.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import yaremax.com.sa_task_04_06.dto.ReportDto;
import yaremax.com.sa_task_04_06.entity.Report;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ReportMapper {
    ReportMapper INSTANCE = Mappers.getMapper(ReportMapper.class);

    Report toEntity(ReportDto dto);

    @Mapping(source = "company.id", target = "companyId")
    ReportDto toDto(Report entity);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    List<ReportDto> toDtoList(List<Report> entityList);
}
