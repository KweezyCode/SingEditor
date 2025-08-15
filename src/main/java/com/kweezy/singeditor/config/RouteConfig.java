package com.kweezy.singeditor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RouteConfig {
    @Singular("rule")
    private List<Rule> rules;

    @JsonProperty("rule_set")
    @Singular("ruleSet")
    private List<RuleSet> ruleSet;

    @JsonProperty("final")
    private String finalTag;

    @JsonProperty("find_process") // TODO: Undocumented feature, report it ASAP!!
    private Boolean findProcess;

    @JsonProperty("auto_detect_interface")
    private Boolean autoDetectInterface;

    @JsonProperty("override_android_vpn")
    private Boolean overrideAndroidVpn;

    @JsonProperty("default_interface")
    private String  defaultInterface;

    @JsonProperty("default_mark")
    private Integer defaultMark;

    @JsonProperty("default_domain_resolver")
    private Object  defaultDomainResolver;

    @JsonProperty("default_network_strategy")
    private String  defaultNetworkStrategy;

    @JsonProperty("default_network_type")
    private List<String> defaultNetworkType;

    @JsonProperty("default_fallback_network_type")
    private List<String> defaultFallbackNetworkType;

    @JsonProperty("default_fallback_delay")
    private String  defaultFallbackDelay;
}
