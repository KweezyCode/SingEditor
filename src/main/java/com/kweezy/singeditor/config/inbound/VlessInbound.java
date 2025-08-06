package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;
import com.kweezy.singeditor.config.common.ListenFields;
import com.kweezy.singeditor.config.common.TlsConfig;
import com.kweezy.singeditor.config.common.MultiplexConfig;
import com.kweezy.singeditor.config.common.TransportConfig;
import java.util.List;

/** VLESS inbound configuration. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class VlessInbound implements TypedInbound {
    private final String type = "vless";
    private String tag;

    private String listen;
    @JsonProperty("listen_port") private Integer listenPort;

    private List<VlessUser> users;
    private TlsConfig tls;
    private MultiplexConfig multiplex;
    private TransportConfig transport;

    @JsonUnwrapped
    private ListenFields listenFields;
}
