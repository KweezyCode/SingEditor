package com.kweezy.singeditor.config.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.DialFields;
import com.kweezy.singeditor.config.common.MultiplexOutboundConfig;
import com.kweezy.singeditor.config.common.UdpOverTcp;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ShadowsocksOutbound implements TypedOutbound {
    private final String type = "shadowsocks";
    private String tag;

    private String  server;
    @JsonProperty("server_port") private Integer serverPort;
    private String  method;
    private String  password;
    private String  network;           // tcp / udp
    @JsonProperty("udp_over_tcp") private UdpOverTcp udpOverTcp;
    private MultiplexOutboundConfig multiplex;
    @JsonUnwrapped
    private DialFields dialFields;
}
