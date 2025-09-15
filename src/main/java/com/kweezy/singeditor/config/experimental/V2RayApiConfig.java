package com.kweezy.singeditor.config.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class V2RayApiConfig {
    private String listen; // gRPC listening address
    private V2RayApiStatsConfig stats; // traffic statistics
}

