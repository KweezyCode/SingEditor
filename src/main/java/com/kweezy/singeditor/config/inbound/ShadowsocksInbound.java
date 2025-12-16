package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.ListenFields;
import com.kweezy.singeditor.config.common.MultiplexInboundConfig;
import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ShadowsocksInbound implements TypedInbound {
    private final String type = "shadowsocks";
    private String tag;

    private String network;
    private String method;
    private String password;
    private List<ShadowsocksUser> users;
    private List<ShadowsocksDestination> destinations;
    private MultiplexInboundConfig multiplex;

    @JsonUnwrapped
    private ListenFields listenFields;
}
