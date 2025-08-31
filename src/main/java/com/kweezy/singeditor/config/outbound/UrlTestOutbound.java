package com.kweezy.singeditor.config.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UrlTestOutbound implements TypedOutbound {
    private final String type = "urltest";
    private String tag;

    private List<String> outbounds;
    private String url;              // optional, default https://www.gstatic.com/generate_204
    private String interval;         // optional, default 3m
    private Integer tolerance;       // optional, default 50 (ms)
    @JsonProperty("idle_timeout") private String idleTimeout; // optional, default 30m
    @JsonProperty("interrupt_exist_connections") private Boolean interruptExistConnections; // optional, default false
}

