package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** listen_fields внутри inbound-ов. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListenFields {
    @JsonProperty("listen")
    private String listen;

    @JsonProperty("listen_port")
    private Integer listenPort;

    @JsonProperty("bind_interface")
    private String bindInterface;

    @JsonProperty("routing_mark")
    private Integer routingMark;

    @JsonProperty("reuse_addr")
    private Boolean reuseAddr;

    @JsonProperty("netns")
    private String netns;

    @JsonProperty("tcp_fast_open")
    private Boolean tcpFastOpen;

    @JsonProperty("tcp_multi_path")
    private Boolean tcpMultiPath;

    @JsonProperty("udp_fragment")
    private Boolean udpFragment;

    @JsonProperty("udp_timeout")
    private String udpTimeout;

    @JsonProperty("detour")
    private String detour;

    /** @deprecated inbound fields are deprecated and will be removed in sing-box 1.13.0 */
    @Deprecated
    @JsonProperty("sniff")
    private Boolean sniff;

    @Deprecated
    @JsonProperty("sniff_override_destination")
    private Boolean sniffOverrideDestination;

    @Deprecated
    @JsonProperty("sniff_timeout")
    private String sniffTimeout;

    @Deprecated
    @JsonProperty("domain_strategy")
    private String domainStrategy;

    @Deprecated
    @JsonProperty("udp_disable_domain_unmapping")
    private Boolean udpDisableDomainUnmapping;
}
