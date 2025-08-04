package com.kweezy.singeditor.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Rule {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("inbound")
    private List<String> inbound;

    @JsonProperty("ip_version")
    private Integer ipVersion;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("network")
    private List<String> network;

    @JsonProperty("auth_user")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("authUser")
    private List<String> authUser;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("protocol")
    private List<String> protocol;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("client")
    private List<String> client;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("domain")
    private List<String> domain;

    @JsonProperty("domain_suffix")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("domainSuffix")
    private List<String> domainSuffix;

    @JsonProperty("domain_keyword")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("domainKeyword")
    private List<String> domainKeyword;

    @JsonProperty("domain_regex")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("domainRegex")
    private List<String> domainRegex;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("geosite")
    private List<String> geosite;

    @JsonProperty("source_geoip")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("sourceGeoip")
    private List<String> sourceGeoip;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("geoip")
    private List<String> geoip;

    @JsonProperty("source_ip_cidr")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("sourceIpCidr")
    private List<String> sourceIpCidr;

    @JsonProperty("source_ip_is_private")
    private Boolean sourceIpIsPrivate;

    @JsonProperty("ip_cidr")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("ipCidr")
    private List<String> ipCidr;

    @JsonProperty("ip_is_private")
    private Boolean ipIsPrivate;

    @JsonProperty("source_port")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("sourcePort")
    private List<Integer> sourcePort;

    @JsonProperty("source_port_range")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("sourcePortRange")
    private List<String> sourcePortRange;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("port")
    private List<Integer> port;

    @JsonProperty("port_range")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("portRange")
    private List<String> portRange;

    @JsonProperty("process_name")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("processName")
    private List<String> processName;

    @JsonProperty("process_path")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("processPath")
    private List<String> processPath;

    @JsonProperty("process_path_regex")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("processPathRegex")
    private List<String> processPathRegex;

    @JsonProperty("package_name")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("packageName")
    private List<String> packageName;

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("user")
    private List<String> user;

    @JsonProperty("user_id")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("userId")
    private List<Integer> userId;

    @JsonProperty("clash_mode")
    private String clashMode;

    @JsonProperty("network_type")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("networkType")
    private List<String> networkType;

    @JsonProperty("network_is_expensive")
    private Boolean networkIsExpensive;

    @JsonProperty("network_is_constrained")
    private Boolean networkIsConstrained;

    @JsonProperty("wifi_ssid")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("wifiSsid")
    private List<String> wifiSsid;

    @JsonProperty("wifi_bssid")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("wifiBssid")
    private List<String> wifiBssid;

    @JsonProperty("rule_set")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("ruleSet")
    private List<String> ruleSet;

    @JsonProperty("rule_set_ipcidr_match_source")
    private Boolean ruleSetIpcidrMatchSource;

    @JsonProperty("rule_set_ip_cidr_match_source")
    private Boolean ruleSetIpCidrMatchSource;

    private Boolean invert;
    private String action;
    private String outbound;

    // Logical rule support
    @JsonProperty("type")
    private String type;

    @JsonProperty("mode")
    private String mode;

    @JsonProperty("rules")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @Singular("rule")
    private List<Rule> rules;
}
