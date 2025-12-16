package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/** Multiplex section for inbounds. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiplexInboundConfig {
    private Boolean enabled;
    private Boolean padding;
    private BrutalConfig brutal;
}
