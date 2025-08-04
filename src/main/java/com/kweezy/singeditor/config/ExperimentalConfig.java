package com.kweezy.singeditor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentalConfig {
    @JsonProperty("cache_file")
    private Map<String, Object> cacheFile;

    @JsonProperty("clash_api")
    private Map<String, Object> clashApi;

    @JsonProperty("v2ray_api")
    private Map<String, Object> v2rayApi;
}
