package com.kweezy.singeditor.config.experimental;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.util.List;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClashApiConfig {
    @JsonProperty("external_controller")
    private String externalController;
    @JsonProperty("external_ui")
    private String externalUi;
    @JsonProperty("external_ui_download_url")
    private String externalUiDownloadUrl;
    @JsonProperty("external_ui_download_detour")
    private String externalUiDownloadDetour;
    private String secret;
    @JsonProperty("default_mode")
    private String defaultMode;
    @JsonProperty("access_control_allow_origin")
    private List<String> accessControlAllowOrigin;
    @JsonProperty("access_control_allow_private_network")
    private Boolean accessControlAllowPrivateNetwork;

    // Deprecated fields (still for compatibility)
    @JsonProperty("store_mode")
    private Boolean storeMode;
    @JsonProperty("store_selected")
    private Boolean storeSelected;
    @JsonProperty("store_fakeip")
    private Boolean storeFakeip;
    @JsonProperty("cache_file")
    private String cacheFile; // deprecated path
    @JsonProperty("cache_id")
    private String cacheId;   // deprecated id
}

