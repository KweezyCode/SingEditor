package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.ListenFields;
import com.kweezy.singeditor.config.common.UdpOverTcp;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class SocksInbound implements TypedInbound {
    private final String type = "socks";
    private String tag;

    private String network;             // tcp / udp
    private String username;
    private String password;
    @JsonProperty("udp_over_tcp") private UdpOverTcp udpOverTcp;
    @JsonUnwrapped
    private ListenFields listenFields;
}
