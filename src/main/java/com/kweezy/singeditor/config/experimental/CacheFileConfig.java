package com.kweezy.singeditor.config.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CacheFileConfig {
    private Boolean enabled;            // enable cache file
    private String path;                // path to cache file (cache.db if empty)
    @JsonProperty("cache_id")
    private String cacheId;             // identifier
    @JsonProperty("store_fakeip")
    private Boolean storeFakeip;        // store fakeip
    @JsonProperty("store_rdrc")
    private Boolean storeRdrc;          // sing-box 1.9.0
    @JsonProperty("rdrc_timeout")
    private String rdrcTimeout;         // timeout string (e.g. 7d)
}

