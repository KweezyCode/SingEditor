package com.kweezy.singeditor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.kweezy.singeditor.config.common.DnsRule;
import com.kweezy.singeditor.config.common.DnsServer;
import com.kweezy.singeditor.config.common.FakeIP;

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

    @JsonProperty("final")
    private String finalServer;

    @JsonProperty("strategy")
    private String strategy;

    @JsonProperty("disable_cache")
    private Boolean disableCache;

    @JsonProperty("disable_expire")
    private Boolean disableExpire;

    @JsonProperty("independent_cache")
    private Boolean independentCache;

    /** Since sing-box 1.9.0 */
    @JsonProperty("client_subnet")
    private String clientSubnet;

    @JsonProperty("fakeip")
    private FakeIP fakeip;
}
