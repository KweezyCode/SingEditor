package com.kweezy.singeditor.config.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.kweezy.singeditor.config.common.DialFields;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class DirectOutbound implements TypedOutbound {
    private final String type = "direct";
    private String tag;

    @JsonProperty("override_address") private String  overrideAddress;
    @JsonProperty("override_port")    private Integer overridePort;
    @JsonProperty("dial_fields")      private DialFields dialFields;
}
