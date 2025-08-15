package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TunInbound.class,   name = "tun"),
        @JsonSubTypes.Type(value = TProxyInbound.class, name = "tproxy"),
        @JsonSubTypes.Type(value = SocksInbound.class, name = "socks"),
        @JsonSubTypes.Type(value = VlessInbound.class, name = "vless"),
        @JsonSubTypes.Type(value = MixedInbound.class, name = "mixed"),
        @JsonSubTypes.Type(value = Hysteria2Inbound.class, name = "hysteria2")
})
public sealed interface TypedInbound permits TunInbound,
        TProxyInbound,
        SocksInbound,
        VlessInbound,
        MixedInbound,
        Hysteria2Inbound {
    String getTag();
}
