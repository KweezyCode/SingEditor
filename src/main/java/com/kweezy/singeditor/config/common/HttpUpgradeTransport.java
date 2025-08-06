package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

/** HTTP Upgrade transport configuration. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class HttpUpgradeTransport implements TransportConfig {
    private final String type = "httpupgrade";
    private String host;
    private String path;
    private Map<String, String> headers;
}
