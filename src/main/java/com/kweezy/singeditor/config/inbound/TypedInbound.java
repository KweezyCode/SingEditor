package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TunInbound.class,   name = "tun"),
        @JsonSubTypes.Type(value = TProxyInbound.class, name = "tproxy"),
        @JsonSubTypes.Type(value = SocksInbound.class, name = "socks")
})
public sealed interface TypedInbound permits TunInbound,
        TProxyInbound,
        SocksInbound {
    String getTag();
}
