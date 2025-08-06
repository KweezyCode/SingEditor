package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

import java.util.List;

import com.kweezy.singeditor.config.common.ListenFields;

/** mixed inbound: SOCKS4/4a/5 and HTTP server. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MixedInbound implements TypedInbound {
    private final String type = "mixed";
    private String tag;

    private List<MixedUser> users;

    @JsonProperty("set_system_proxy")
    private Boolean setSystemProxy;

    @JsonUnwrapped
    private ListenFields listenFields;
}
