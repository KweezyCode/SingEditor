package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kweezy.singeditor.config.common.ListenFields;
import com.kweezy.singeditor.config.common.UdpOverTcp;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TProxyInbound implements TypedInbound {
    private final String type = "tproxy";
    private String tag;

    private String listen;
    @JsonProperty("listen_port") private Integer listenPort;
    private String network;             // tcp / udp
    @JsonProperty("udp_over_tcp") private UdpOverTcp udpOverTcp;
    private Boolean transparent;
    @JsonProperty("listen_fields") private ListenFields listenFields;
}
