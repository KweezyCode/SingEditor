package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Deprecated in sing-box 1.12.0.
 * Legacy fake-ip configuration will be removed in sing-box 1.14.0.
 */
@Deprecated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FakeIP {
    @JsonProperty("enabled")
    private Boolean enabled;

    @JsonProperty("inet4_range")
    private String inet4Range;

    @JsonProperty("inet6_range")
    private String inet6Range;
}
