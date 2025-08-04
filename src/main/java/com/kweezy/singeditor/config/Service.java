package com.kweezy.singeditor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Service {
    private String                type;
    private String                tag;
    private Map<String, Object> settings;
}