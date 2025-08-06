package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/** Общая секция multiplex в outbound-ах. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiplexConfig {
    private Boolean enabled;                    // включить/выключить
    private String  protocol;                   // smux | yamux
    @JsonProperty("max_connections")
    private Integer maxConnections;

    @JsonProperty("min_streams")
    private Integer minStreams;

    @JsonProperty("max_streams")
    private Integer maxStreams;

    private Boolean padding;

    private BrutalConfig brutal;
}
