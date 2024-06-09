package yaremax.com.sa_task_04_06.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import yaremax.com.sa_task_04_06.dto.CompanyDto;
import yaremax.com.sa_task_04_06.entity.Company;

import java.util.List;

@Mapper
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    Company toEntity(CompanyDto dto);
    CompanyDto toDto(Company company);
    List<CompanyDto> toDtoList(List<Company> entityList);
}