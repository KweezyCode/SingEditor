package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/** Поле dial_fields (общее для outbound-ов, NTP и т.д.). */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DialFields {
    @JsonProperty("detour")
    private String detour;

    @JsonProperty("bind_interface")
    private String bindInterface;

    @JsonProperty("inet4_bind_address")
    private String inet4BindAddress;

    @JsonProperty("inet6_bind_address")
    private String inet6BindAddress;

    @JsonProperty("routing_mark")
    private Integer routingMark;

    @JsonProperty("reuse_addr")
    private Boolean reuseAddr;

    @JsonProperty("netns")
    private String netns;

    @JsonProperty("connect_timeout")
    private String connectTimeout;

    @JsonProperty("tcp_fast_open")
    private Boolean tcpFastOpen;

    @JsonProperty("tcp_multi_path")
    private Boolean tcpMultiPath;

    @JsonProperty("udp_fragment")
    private Boolean udpFragment;

    @JsonProperty("domain_resolver")
    private Object domainResolver;

    @JsonProperty("network_strategy")
    private String networkStrategy;

    @JsonProperty("network_type")
    private List<String> networkType;

    @JsonProperty("fallback_network_type")
    private List<String> fallbackNetworkType;

    @JsonProperty("fallback_delay")
    private String fallbackDelay;

    /** @deprecated to be removed in sing-box 1.14.0 */
    @Deprecated
    @JsonProperty("domain_strategy")
    private String domainStrategy;
}
