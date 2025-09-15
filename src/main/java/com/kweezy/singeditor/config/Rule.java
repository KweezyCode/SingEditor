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

    @Singular("inbound")
    private List<String> inbound;

    @JsonProperty("ip_version")
    private Integer ipVersion;


    @Singular("network")
    private List<String> network;

    @JsonProperty("auth_user")

    @Singular("authUser")
    private List<String> authUser;


    @Singular("protocol")
    private List<String> protocol;


    @Singular("client")
    private List<String> client;


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


    @Singular("geosite")
    private List<String> geosite;

    @JsonProperty("source_geoip")

    @Singular("sourceGeoip")
    private List<String> sourceGeoip;


    @Singular("geoip")
    private List<String> geoip;

    @JsonProperty("source_ip_cidr")

    @Singular("sourceIpCidr")
    private List<String> sourceIpCidr;

    @JsonProperty("source_ip_is_private")
    private Boolean sourceIpIsPrivate;

    @JsonProperty("ip_cidr")

    @Singular("ipCidr")
    private List<String> ipCidr;

    @JsonProperty("ip_is_private")
    private Boolean ipIsPrivate;

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
    private List<String> processPathRegex;

    @JsonProperty("package_name")

    @Singular("packageName")
    private List<String> packageName;


    @Singular("user")
    private List<String> user;

    @JsonProperty("user_id")

    @Singular("userId")
    private List<Integer> userId;

    @JsonProperty("clash_mode")
    private String clashMode;

    @JsonProperty("network_type")

    @Singular("networkType")
    private List<String> networkType;

    @JsonProperty("network_is_expensive")
    private Boolean networkIsExpensive;

    @JsonProperty("network_is_constrained")
    private Boolean networkIsConstrained;

    @JsonProperty("wifi_ssid")

    @Singular("wifiSsid")
    private List<String> wifiSsid;

    @JsonProperty("wifi_bssid")

    @Singular("wifiBssid")
    private List<String> wifiBssid;

    @JsonProperty("rule_set")

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

    @Singular("rule")
    private List<Rule> rules;
}
