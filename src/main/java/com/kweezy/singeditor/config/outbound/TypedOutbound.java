package com.kweezy.singeditor.config.outbound;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectOutbound.class,      name = "direct"),
        @JsonSubTypes.Type(value = BlockOutbound.class,       name = "block"),
        @JsonSubTypes.Type(value = VlessOutbound.class,       name = "vless"),
        @JsonSubTypes.Type(value = ShadowsocksOutbound.class, name = "shadowsocks"),
        @JsonSubTypes.Type(value = DNSOutbound.class,         name = "dns"),
        @JsonSubTypes.Type(value = Hysteria2Outbound.class,    name = "hysteria2")
})
public sealed interface TypedOutbound permits DirectOutbound,
        BlockOutbound,
        VlessOutbound,
        ShadowsocksOutbound,
        DNSOutbound,
        Hysteria2Outbound {
    String getTag();
}
