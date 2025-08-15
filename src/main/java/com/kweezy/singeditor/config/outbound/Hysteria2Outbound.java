package com.kweezy.singeditor.config.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.DialFields;
import com.kweezy.singeditor.config.common.TlsOutboundConfig;
import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Hysteria2Outbound implements TypedOutbound {
    private final String type = "hysteria2";
    private String tag;

    private String server;
    @JsonProperty("server_port") private Integer serverPort;
    @JsonProperty("server_ports") private List<String> serverPorts;
    @JsonProperty("hop_interval") private String hopInterval;

    @JsonProperty("up_mbps") private Integer upMbps;
    @JsonProperty("down_mbps") private Integer downMbps;

    private ObfsConfig obfs;

    private String password;
    private String network;

    private TlsOutboundConfig tls;

    @JsonProperty("brutal_debug") private Boolean brutalDebug;

    @JsonUnwrapped
    private DialFields dialFields;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ObfsConfig {
        private String type;
        private String password;
    }
}
