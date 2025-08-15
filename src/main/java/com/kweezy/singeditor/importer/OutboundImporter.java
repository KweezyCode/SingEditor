package com.kweezy.singeditor.importer;

import com.kweezy.singeditor.config.outbound.TypedOutbound;

import java.util.Optional;

public interface OutboundImporter {
    boolean supports(String line);
    Optional<TypedOutbound> parse(String line) throws Exception;
}

