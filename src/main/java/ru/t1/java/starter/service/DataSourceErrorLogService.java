package ru.t1.java.starter.service;

import ru.t1.java.starter.dto.DataSourceErrorLogDto;

import java.util.List;

public interface DataSourceErrorLogService {
    List<DataSourceErrorLogDto> getAllDataSourceErrors();

    DataSourceErrorLogDto getDataSourceErrorById(long id);

    DataSourceErrorLogDto saveDataSourceError(DataSourceErrorLogDto DataSourceErrorLogDto, String kafkaMessage);

    void deleteDataSourceErrorById(long id);
}
