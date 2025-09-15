package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnsServer {
    private String              type;
    private String              tag;
    private Map<String, Object> settings;
    // Added explicit fields from examples
    private String              server;           // e.g. "1.1.1.1"
    private String              detour;           // e.g. outbound tag
    @JsonProperty("domain_resolver")
    private String              domainResolver;   // e.g. "dns-local"
}