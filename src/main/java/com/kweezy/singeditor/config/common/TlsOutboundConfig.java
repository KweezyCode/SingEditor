package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TlsOutboundConfig {
    private Boolean enabled;
    @JsonProperty("disable_sni") private Boolean disableSni;
    @JsonProperty("server_name") private String serverName;
    private Boolean insecure;
    private List<String> alpn;
    @JsonProperty("min_version") private String minVersion;
    @JsonProperty("max_version") private String maxVersion;
    @JsonProperty("cipher_suites") private List<String> cipherSuites;
    private String certificate;
    @JsonProperty("certificate_path") private String certificatePath;

    // TLS fragmentation options (client only, since sing-box 1.12.0)
    private Boolean fragment;
    @JsonProperty("fragment_fallback_delay") private String fragmentFallbackDelay;
    @JsonProperty("record_fragment") private Boolean recordFragment;

    private EchConfigOutbound ech;
    private UtlsConfig utls;
    private RealityConfigOutbound reality;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EchConfigOutbound {
        private Boolean enabled;
        private List<String> config;
        @JsonProperty("config_path") private String configPath;
        @Deprecated @JsonProperty("pq_signature_schemes_enabled") private Boolean pqSignatureSchemesEnabled;
        @Deprecated @JsonProperty("dynamic_record_sizing_disabled") private Boolean dynamicRecordSizingDisabled;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UtlsConfig {
        private Boolean enabled;
        private String fingerprint;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RealityConfigOutbound {
        private Boolean enabled;
        @JsonProperty("public_key") private String publicKey;
        @JsonProperty("short_id") private String shortId;
    }
}
