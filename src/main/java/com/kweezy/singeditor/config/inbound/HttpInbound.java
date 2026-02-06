package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.ListenFields;
import com.kweezy.singeditor.config.common.TlsInboundConfig;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class HttpInbound implements TypedInbound {
    private final String type = "http";
    private String tag;

    @JsonUnwrapped
    private ListenFields listenFields;

    private List<HttpUser> users;
    private TlsInboundConfig tls;

    @JsonProperty("set_system_proxy")
    private Boolean setSystemProxy;
}
