package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

/** HTTP transport configuration. */
@Data @Builder @NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class HttpTransport implements TransportConfig {
    private final String type = "http";
    private List<String> host;
    private String path;
    private String method;
    private Map<String, String> headers;
    @JsonProperty("idle_timeout")
    private String idleTimeout;
    @JsonProperty("ping_timeout")
    private String pingTimeout;
}
