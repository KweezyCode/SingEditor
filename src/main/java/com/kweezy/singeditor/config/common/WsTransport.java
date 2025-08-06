package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

/** WebSocket transport configuration. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class WsTransport implements TransportConfig {
    private final String type = "ws";
    private String path;
    private Map<String, String> headers;
    @JsonProperty("max_early_data")
    private Integer maxEarlyData;
    @JsonProperty("early_data_header_name")
    private String earlyDataHeaderName;
}
