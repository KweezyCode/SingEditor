package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** Поле dial_fields (общее для outbound-ов, NTP и т.д.). */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DialFields {
    @JsonProperty("interface_name")
    private String  interfaceName;
    private Integer mark;
    @JsonProperty("interface_strategy")
    private String  interfaceStrategy;          // ipv4_only / ipv6_only / prefer_ipv6 …
}
