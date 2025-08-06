package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/** QUIC transport configuration. */
@Data
@Builder
@NoArgsConstructor
// @AllArgsConstructor (No fields here, so no need for this)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class QuicTransport implements TransportConfig {
    private final String type = "quic";
}
