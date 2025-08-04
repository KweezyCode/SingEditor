package com.kweezy.singeditor.config;

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
public class RuleSet {
    private String type;
    private String tag;

    // Inline rules list
    @JsonProperty("rules")
    @Singular("rule")
    private List<Rule> rules;

    // Local or remote fields
    private String format;
    private String path;
    private String url;

    @JsonProperty("download_detour")
    private String downloadDetour;

    @JsonProperty("update_interval")
    private String updateInterval;
}
