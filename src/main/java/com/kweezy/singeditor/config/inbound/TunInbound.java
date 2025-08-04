package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.ListenFields;
import com.kweezy.singeditor.config.common.PlatformConfig;
import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TunInbound implements TypedInbound {
    private final String type = "tun";
    private String tag;

    @JsonProperty("interface_name") private String interfaceName;
    private List<String> address;
    private Integer mtu;
    @JsonProperty("auto_route") private Boolean autoRoute;
    private PlatformConfig platform;
    @JsonUnwrapped
    private ListenFields listenFields;
}
