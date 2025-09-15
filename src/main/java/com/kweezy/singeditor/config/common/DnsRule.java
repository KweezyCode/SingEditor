package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnsRule {
    // Matching fields
    @Singular("inbound")
    private List<String> inbound;

    @JsonProperty("ip_version")
    private Integer ipVersion; // 4 / 6

    @JsonProperty("query_type")
    @Singular("queryType")
    private List<Object> queryType; // integers or strings

    @Singular("network")
    private List<String> network; // tcp / udp

    @JsonProperty("auth_user")
    @Singular("authUser")
    private List<String> authUser;

    @Singular("protocol")
    private List<String> protocol; // sniffed protocols

    @Singular("domain")
    private List<String> domain;

    @JsonProperty("domain_suffix")
    @Singular("domainSuffix")
    private List<String> domainSuffix;

    @JsonProperty("domain_keyword")
    @Singular("domainKeyword")
    private List<String> domainKeyword;

    @JsonProperty("domain_regex")
    @Singular("domainRegex")
    private List<String> domainRegex;

    // Deprecated (will be removed >=1.12) but keep for backward compatibility
    @Singular("geosite")
    private List<String> geosite; // deprecated 1.8 removed 1.12

    @JsonProperty("source_geoip")
    @Singular("sourceGeoip")
    private List<String> sourceGeoip; // deprecated

    @Singular("geoip")
    private List<String> geoip; // deprecated removed 1.12

    @JsonProperty("source_ip_cidr")
    @Singular("sourceIpCidr")
    private List<String> sourceIpCidr;

    @JsonProperty("source_ip_is_private")
    private Boolean sourceIpIsPrivate;

    @JsonProperty("ip_cidr")
    @Singular("ipCidr")
    private List<String> ipCidr; // address filter

    @JsonProperty("ip_is_private")
    private Boolean ipIsPrivate; // address filter

    @JsonProperty("ip_accept_any")
    private Boolean ipAcceptAny; // since 1.12.0 address filter

    @JsonProperty("source_port")
    @Singular("sourcePort")
    private List<Integer> sourcePort;

    @JsonProperty("source_port_range")
    @Singular("sourcePortRange")
    private List<String> sourcePortRange;

    @Singular("port")
    private List<Integer> port;

    @JsonProperty("port_range")
    @Singular("portRange")
    private List<String> portRange;

    @JsonProperty("process_name")
    @Singular("processName")
    private List<String> processName;

    @JsonProperty("process_path")
    @Singular("processPath")
    private List<String> processPath;

    @JsonProperty("process_path_regex")
    @Singular("processPathRegex")
    private List<String> processPathRegex; // since 1.10.0

    @JsonProperty("package_name")
    @Singular("packageName")
    private List<String> packageName; // Android

    @Singular("user")
    private List<String> user; // Linux user name

    @JsonProperty("user_id")
    @Singular("userId")
    private List<Integer> userId; // Linux user id

    @JsonProperty("clash_mode")
    private String clashMode;

    @JsonProperty("network_type")
    @Singular("networkType")
    private List<String> networkType; // since 1.11.0

    @JsonProperty("network_is_expensive")
    private Boolean networkIsExpensive; // since 1.11.0

    @JsonProperty("network_is_constrained")
    private Boolean networkIsConstrained; // since 1.11.0

    @JsonProperty("interface_address")
    private Map<String, List<String>> interfaceAddress; // since 1.13.0

    @JsonProperty("network_interface_address")
    private Map<String, List<String>> networkInterfaceAddress; // since 1.13.0

    @JsonProperty("default_interface_address")
    @Singular("defaultInterfaceAddress")
    private List<String> defaultInterfaceAddress; // since 1.13.0

    @JsonProperty("wifi_ssid")
    @Singular("wifiSsid")
    private List<String> wifiSsid;

    @JsonProperty("wifi_bssid")
    @Singular("wifiBssid")
    private List<String> wifiBssid;

    @JsonProperty("rule_set")
    @Singular("ruleSet")
    private List<String> ruleSet; // since 1.8.0

    @JsonProperty("rule_set_ipcidr_match_source")
    private Boolean ruleSetIpcidrMatchSource; // deprecated rename 1.10.0

    @JsonProperty("rule_set_ip_cidr_match_source")
    private Boolean ruleSetIpCidrMatchSource; // since 1.10.0

    @JsonProperty("rule_set_ip_cidr_accept_empty")
    private Boolean ruleSetIpCidrAcceptEmpty; // since 1.10.0

    private Boolean invert;

    // Deprecated outbound (will be removed 1.14.0) kept as list (spec shows array)
    @Singular("outbound")
    private List<String> outbound; // deprecated since 1.12.0

    // Action (required)
    private String action; // e.g., route, reject, predefined, etc.

    // Fields for action = predefined
    private String answer; // e.g. "localhost. IN A 127.0.0.1"
    private String rcode;  // e.g. NOERROR

    // Deprecated server/disable_cache/rewrite_ttl/client_subnet moved to action parameters in 1.11.0
    private String server; // deprecated

    @JsonProperty("disable_cache")
    private Boolean disableCache; // deprecated

    @JsonProperty("rewrite_ttl")
    private Integer rewriteTtl; // deprecated

    @JsonProperty("client_subnet")
    private String clientSubnet; // deprecated (moved)

    // Logical rule support (type=logical)
    private String type; // "logical"
    private String mode; // and / or

    @Singular("rule")
    private List<DnsRule> rules; // nested logical rules
}
