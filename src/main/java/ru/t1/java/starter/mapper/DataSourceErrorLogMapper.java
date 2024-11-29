package ru.t1.java.starter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ru.t1.java.starter.dto.DataSourceErrorLogDto;
import ru.t1.java.starter.model.DataSourceErrorLog;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface DataSourceErrorLogMapper {
    DataSourceErrorLogMapper INSTANCE = Mappers.getMapper(DataSourceErrorLogMapper.class);

    DataSourceErrorLog toEntity(DataSourceErrorLogDto dataSourceErrorLogDto);

    DataSourceErrorLogDto toDTO(DataSourceErrorLog dataSourceErrorLog);
}
