package com.kweezy.singeditor.config.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentalConfig {
    @JsonProperty("cache_file")
    private CacheFileConfig cacheFile;

    @JsonProperty("clash_api")
    private ClashApiConfig clashApi;

    @JsonProperty("v2ray_api")
    private V2RayApiConfig v2rayApi;
}
