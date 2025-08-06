package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** gRPC transport configuration. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class GrpcTransport implements TransportConfig {
    private final String type = "grpc";
    @JsonProperty("service_name")
    private String serviceName;
    @JsonProperty("idle_timeout")
    private String idleTimeout;
    @JsonProperty("ping_timeout")
    private String pingTimeout;
    @JsonProperty("permit_without_stream")
    private Boolean permitWithoutStream;
}
