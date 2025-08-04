package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** Платформенные опции для Tun inbound. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlatformConfig {
    private Boolean netstack;                   // использовать userspace-стек

    @JsonProperty("http_proxy")
    private HttpProxyConfig httpProxy;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class HttpProxyConfig {
        private Boolean enabled;
        @JsonProperty("server_port")
        private Integer serverPort;
    }
}
