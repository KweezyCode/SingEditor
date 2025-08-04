package com.kweezy.singeditor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CertificateConfig {
    private List<String> certificate;

    @JsonProperty("certificate_path")
    private List<String> certificatePath;

    @JsonProperty("certificate_directory_path")
    private String      certificateDirectoryPath;
}
