package com.kweezy.singeditor.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import com.kweezy.singeditor.config.inbound.TypedInbound;
import com.kweezy.singeditor.config.outbound.TypedOutbound;
import com.kweezy.singeditor.config.endpoint.TypedEndpoint;

import java.util.List;

/** Sing-box root configuration POJO (2025-08 docs). */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SingBoxConfig {
    private LogConfig log;
    private DnsConfig dns;
    private NtpConfig ntp;
    private CertificateConfig certificate;

    @Singular private List<TypedEndpoint> endpoints;

    @Singular private List<TypedInbound>  inbounds;
    @Singular private List<TypedOutbound> outbounds;

    private RouteConfig route;
    @Singular private List<Service> services;
    private ExperimentalConfig experimental;
}
