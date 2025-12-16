package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.*;
import lombok.*;

import java.util.List;

/** VLESS inbound configuration. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class VlessInbound implements TypedInbound {
    private final String type = "vless";
    private String tag;

    private List<VlessUser> users;
    private TlsInboundConfig tls;
    private MultiplexInboundConfig multiplex;
    private TransportConfig transport;

    @JsonUnwrapped
    private ListenFields listenFields;
}
