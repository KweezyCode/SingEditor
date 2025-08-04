package com.kweezy.singeditor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LogConfig {
    private Boolean disabled;
    private String level;
    private String output;
    private Boolean timestamp;
}
