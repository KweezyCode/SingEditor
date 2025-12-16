package com.kweezy.singeditor.config.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.DialFields;
import com.kweezy.singeditor.config.common.UdpOverTcp;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class SocksOutbound implements TypedOutbound {
    private final String type = "socks";
    private String tag;

    private String server;
    @JsonProperty("server_port")
    private Integer serverPort;
    private String version; // "4", "4a", "5"
    private String username;
    private String password;
    private String network; // "tcp", "udp"
    @JsonProperty("udp_over_tcp")
    private UdpOverTcp udpOverTcp;

    @JsonUnwrapped
    private DialFields dialFields;
}
