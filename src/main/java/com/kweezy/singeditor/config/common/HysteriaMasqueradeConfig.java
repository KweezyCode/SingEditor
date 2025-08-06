package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HysteriaMasqueradeConfig {
    /** Type of masquerade: file, proxy, string */
    private String type;

    /** For file type: root directory */
    private String directory;

    /** For proxy type: target URL */
    private String url;

    /** For proxy type: rewrite Host header */
    @JsonProperty("rewrite_host")
    private Boolean rewriteHost;

    /** For string type: fixed response status code */
    @JsonProperty("status_code")
    private Integer statusCode;

    /** For string type: fixed response headers */
    private Map<String, String> headers;

    /** For string type: fixed response content */
    private String content;
}
