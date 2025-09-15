package com.kweezy.singeditor.config.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.List;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class V2RayApiStatsConfig {
    private Boolean enabled;
    private List<String> inbounds;
    private List<String> outbounds;
    private List<String> users;
}

