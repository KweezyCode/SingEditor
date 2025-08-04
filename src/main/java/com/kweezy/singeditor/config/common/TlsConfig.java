package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/** Блок TLS для VLESS/VMess/HTTP и т.д. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TlsConfig {
    private Boolean       enabled;
    private List<String>  alpn;                 // ["h2","http/1.1"]
    @JsonProperty("min_version")
    private String        minVersion;           // "1.2" | "1.3"
    private Boolean       insecure;
    @JsonProperty("server_name")
    private String        serverName;
}
