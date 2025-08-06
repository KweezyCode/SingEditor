package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TlsInboundConfig {
    private Boolean enabled;
    @JsonProperty("server_name") private String serverName;
    private List<String> alpn;
    @JsonProperty("min_version") private String minVersion;
    @JsonProperty("max_version") private String maxVersion;
    @JsonProperty("cipher_suites") private List<String> cipherSuites;
    private List<String> certificate;
    @JsonProperty("certificate_path") private String certificatePath;
    private List<String> key;
    @JsonProperty("key_path") private String keyPath;

    private AcmeConfig acme;
    private EchConfigInbound ech;
    private RealityConfigInbound reality;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AcmeConfig {
        private List<String> domain;
        @JsonProperty("data_directory") private String dataDirectory;
        @JsonProperty("default_server_name") private String defaultServerName;
        private String email;
        private String provider;
        @JsonProperty("disable_http_challenge") private Boolean disableHttpChallenge;
        @JsonProperty("disable_tls_alpn_challenge") private Boolean disableTlsAlpnChallenge;
        @JsonProperty("alternative_http_port") private Integer alternativeHttpPort;
        @JsonProperty("alternative_tls_port") private Integer alternativeTlsPort;
        @JsonProperty("external_account") private ExternalAccount externalAccount;
        @JsonProperty("dns01_challenge") private Object dns01Challenge;

        @Data @Builder @NoArgsConstructor @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class ExternalAccount {
            @JsonProperty("key_id") private String keyId;
            @JsonProperty("mac_key") private String macKey;
        }
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EchConfigInbound {
        private Boolean enabled;
        private List<String> key;
        @JsonProperty("key_path") private String keyPath;
        @Deprecated @JsonProperty("pq_signature_schemes_enabled") private Boolean pqSignatureSchemesEnabled;
        @Deprecated @JsonProperty("dynamic_record_sizing_disabled") private Boolean dynamicRecordSizingDisabled;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RealityConfigInbound {
        private Boolean enabled;
        private Handshake handshake;
        @JsonProperty("private_key") private String privateKey;
        @JsonProperty("short_id") private List<String> shortId;
        @JsonProperty("max_time_difference") private String maxTimeDifference;

        @Data @Builder @NoArgsConstructor @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Handshake {
            private String server;
            @JsonProperty("server_port") private Integer serverPort;
            @JsonUnwrapped private DialFields dialFields;
        }
    }
}
