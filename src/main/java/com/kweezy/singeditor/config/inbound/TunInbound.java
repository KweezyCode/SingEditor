package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.kweezy.singeditor.config.common.ListenFields;
import com.kweezy.singeditor.config.common.PlatformConfig;
import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TunInbound implements TypedInbound {
    private final String type = "tun";
    private String tag;

    @JsonProperty("interface_name") private String interfaceName;
    
    private List<String> address;
    private Integer mtu;
    @JsonProperty("auto_route") private Boolean autoRoute;

    // Tun-specific fields since sing-box 1.10.0
    @JsonProperty("iproute2_table_index") private Integer iproute2TableIndex;
    @JsonProperty("iproute2_rule_index") private Integer iproute2RuleIndex;

    @JsonProperty("auto_redirect") private Boolean autoRedirect;
    @JsonProperty("auto_redirect_input_mark") private String autoRedirectInputMark;
    @JsonProperty("auto_redirect_output_mark") private String autoRedirectOutputMark;

    @JsonProperty("loopback_address")
    
    private List<String> loopbackAddress;
    @JsonProperty("strict_route") private Boolean strictRoute;

    @JsonProperty("route_address")
    
    private List<String> routeAddress;
    @JsonProperty("route_exclude_address")
    
    private List<String> routeExcludeAddress;
    @JsonProperty("route_address_set")
    
    private List<String> routeAddressSet;
    @JsonProperty("route_exclude_address_set")
    
    private List<String> routeExcludeAddressSet;

    @JsonProperty("endpoint_independent_nat") private Boolean endpointIndependentNat;
    @JsonProperty("udp_timeout") private String udpTimeout;
    private String stack;

    @JsonProperty("include_interface")
    
    private List<String> includeInterface;
    @JsonProperty("exclude_interface")
    
    private List<String> excludeInterface;

    @JsonProperty("include_uid")
    
    private List<Integer> includeUid;
    @JsonProperty("include_uid_range")
    
    private List<String> includeUidRange;
    @JsonProperty("exclude_uid")
    
    private List<Integer> excludeUid;
    @JsonProperty("exclude_uid_range")
    
    private List<String> excludeUidRange;

    @JsonProperty("include_android_user")
    
    private List<Integer> includeAndroidUser;
    @JsonProperty("include_package")
    
    private List<String> includePackage;
    @JsonProperty("exclude_package")
    
    private List<String> excludePackage;

    // Deprecated fields
    @Deprecated private Boolean gso;
    @Deprecated @JsonProperty("inet4_address") private List<String> inet4Address;
    @Deprecated @JsonProperty("inet6_address") private List<String> inet6Address;
    @Deprecated @JsonProperty("inet4_route_address") private List<String> inet4RouteAddress;
    @Deprecated @JsonProperty("inet6_route_address") private List<String> inet6RouteAddress;
    @Deprecated @JsonProperty("inet4_route_exclude_address") private List<String> inet4RouteExcludeAddress;
    @Deprecated @JsonProperty("inet6_route_exclude_address") private List<String> inet6RouteExcludeAddress;

    private PlatformConfig platform;
    @JsonUnwrapped
    private ListenFields listenFields;
}
