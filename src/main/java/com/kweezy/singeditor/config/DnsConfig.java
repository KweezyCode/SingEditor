package com.kweezy.singeditor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.kweezy.singeditor.config.common.DnsRule;
import com.kweezy.singeditor.config.common.DnsServer;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnsConfig {
    /**
     * Since sing-box 1.11.0
     */
    @JsonProperty("cache_capacity")
    private Integer cacheCapacity;

    @JsonProperty("reverse_mapping")
    private Boolean reverseMapping;

    @Singular
    private List<DnsServer> servers;

    @Singular
    private List<DnsRule> rules;
}
