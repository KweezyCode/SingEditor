package com.kweezy.singeditor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import com.kweezy.singeditor.config.common.DialFields;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NtpConfig {
    private Boolean enabled;
    private String  server;

    @JsonProperty("server_port")
    private Integer serverPort;

    /** ISO-8601 duration, e.g. “30m” */
    private String interval;

    @JsonProperty("dial_fields")
    private DialFields dialFields;
}
