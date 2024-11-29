package ru.t1.java.starter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutionTimeMetric {

    private long executionTime;

    private String methodSignature;

    private List<Object> args;
}
