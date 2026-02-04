package com.kweezy.singeditor.importer;

import com.kweezy.singeditor.config.SingBoxConfig;
import com.kweezy.singeditor.config.outbound.TypedOutbound;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ImportService {
    private final List<OutboundImporter> importers;

    public ImportService(List<OutboundImporter> importers) {
        Objects.requireNonNull(importers, "importers");
        this.importers = List.copyOf(importers);
    }

    public static ImportService defaultService() {
        return new ImportService(defaultImporters());
    }

    public static List<OutboundImporter> defaultImporters() {
        return List.of(new VlessImporter(), new ShadowsocksImporter());
    }

    public ImportResult importText(String text, SingBoxConfig cfg) {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(cfg, "cfg");
        if (text.isBlank()) return new ImportResult(0, 0, List.of());
        String[] lines = text.split("\r?\n");
        int imported = 0, failed = 0;
        List<String> errors = new ArrayList<>();
        ensureOutboundsList(cfg);
        Set<String> existingTags = collectExistingTags(cfg);

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) continue;
            Optional<OutboundImporter> importer = importers.stream().filter(im -> im.supports(line)).findFirst();
            if (importer.isEmpty()) {
                failed++; errors.add("Unsupported line: " + line); continue;
            }
            try {
                Optional<TypedOutbound> parsed = importer.get().parse(line);
                if (parsed.isEmpty()) { failed++; errors.add("Parse error: " + line); continue; }
                TypedOutbound outbound = parsed.get();
                
                // Always use sequential numbering: proxyN
                String tag = nextProxyTag(existingTags);
                
                setTag(outbound, tag);
                existingTags.add(tag);
                cfg.getOutbounds().add(outbound);
                imported++;
            } catch (Exception ex) {
                failed++; errors.add("Error: " + ex.getMessage());
            }
        }
        return new ImportResult(imported, failed, errors);
    }

    private void ensureOutboundsList(SingBoxConfig cfg) {
        if (cfg.getOutbounds() == null) cfg.setOutbounds(new ArrayList<>());
    }

    private Set<String> collectExistingTags(SingBoxConfig cfg) {
        Set<String> set = new HashSet<>();
        if (cfg.getOutbounds() != null) {
            for (TypedOutbound to : cfg.getOutbounds()) {
                String t = getTag(to);
                if (t != null) set.add(t);
            }
        }
        return set;
    }

    private static String nextProxyTag(Set<String> existing) {
        int i = 1;
        String tag = "proxy" + i;
        while (existing.contains(tag)) {
            i++;
            tag = "proxy" + i;
        }
        return tag;
    }

    private static String getTag(TypedOutbound to) {
        return to.getTag();
    }

    private static void setTag(TypedOutbound to, String tag) {
        try {
            to.getClass().getMethod("setTag", String.class).invoke(to, tag);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Outbound does not expose setTag(String)", ex);
        }
    }
}
