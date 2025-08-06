package com.kweezy.singeditor.config.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.DialFields;
import com.kweezy.singeditor.config.common.MultiplexConfig;
import com.kweezy.singeditor.config.common.TlsOutboundConfig;
import com.kweezy.singeditor.config.common.TransportConfig;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class VlessOutbound implements TypedOutbound {
    private final String type = "vless";
    private String tag;

    private String server;
    @JsonProperty("server_port") private Integer serverPort;
    private String uuid;
    private String flow;
    private String network;
    @JsonProperty("packet_encoding") private String packetEncoding;
    private TlsOutboundConfig tls;
    private MultiplexConfig multiplex;
    private TransportConfig transport;
    @JsonUnwrapped
    private DialFields dialFields;
}
