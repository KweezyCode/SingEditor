package com.kweezy.singeditor.config.endpoint;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class WireguardPeer {
    private String address;
    private Integer port;
    @JsonProperty("public_key")
    private String publicKey;
    @JsonProperty("pre_shared_key")
    private String preSharedKey;
    @JsonProperty("allowed_ips")
    private List<String> allowedIps;
    @JsonProperty("persistent_keepalive_interval")
    private Integer persistentKeepaliveInterval;
    private List<Integer> reserved;
}

