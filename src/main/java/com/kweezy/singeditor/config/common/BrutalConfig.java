package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** TCP Brutal configuration section. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BrutalConfig {
    private Boolean enabled;

    @JsonProperty("up_mbps")
    private Integer upMbps;

    @JsonProperty("down_mbps")
    private Integer downMbps;
}
