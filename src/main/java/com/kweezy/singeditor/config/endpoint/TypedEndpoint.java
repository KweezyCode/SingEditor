package com.kweezy.singeditor.config.endpoint;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = WireguardEndpoint.class, name = "wireguard")
})
public sealed interface TypedEndpoint permits WireguardEndpoint {
    String getTag();
}

