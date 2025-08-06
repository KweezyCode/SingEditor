package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.ListenFields;
import com.kweezy.singeditor.config.common.TlsInboundConfig;
import com.kweezy.singeditor.config.common.HysteriaMasqueradeConfig;
import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class HysteriaInbound implements TypedInbound {
    private final String type = "hysteria2";
    private String tag;

    @JsonProperty("up_mbps") private Integer upMbps;
    @JsonProperty("down_mbps") private Integer downMbps;

    private ObfsConfig obfs;

    private List<HysteriaUser> users;

    @JsonProperty("ignore_client_bandwidth") private Boolean ignoreClientBandwidth;

    private TlsInboundConfig tls;

    private HysteriaMasqueradeConfig masquerade;

    @JsonProperty("brutal_debug") private Boolean brutalDebug;

    @JsonUnwrapped private ListenFields listenFields;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ObfsConfig {
        private String type;
        private String password;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class HysteriaUser {
        private String name;
        private String password;
    }
}
