package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HttpTransport.class, name = "http"),
    @JsonSubTypes.Type(value = WsTransport.class, name = "ws"),
    @JsonSubTypes.Type(value = QuicTransport.class, name = "quic"),
    @JsonSubTypes.Type(value = GrpcTransport.class, name = "grpc"),
    @JsonSubTypes.Type(value = HttpUpgradeTransport.class, name = "httpupgrade")
})
public sealed interface TransportConfig permits HttpTransport, WsTransport, QuicTransport, GrpcTransport, HttpUpgradeTransport {
}
