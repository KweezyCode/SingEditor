package com.kweezy.singeditor.config.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.DialFields;
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
public final class WireguardEndpoint implements TypedEndpoint {
    private final String type = "wireguard";
    private String tag;

    private Boolean system;
    private String name;
    private Integer mtu;
    private List<String> address;

    @JsonProperty("private_key")
    private String privateKey;

    @JsonProperty("listen_port")
    private Integer listenPort;

    private List<WireguardPeer> peers;

    @JsonProperty("udp_timeout")
    private String udpTimeout;
    private Integer workers;

    @JsonUnwrapped
    private DialFields dialFields;
}
