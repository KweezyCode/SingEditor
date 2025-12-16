package com.kweezy.singeditor.importer;

import com.kweezy.singeditor.config.SingBoxConfig;
import com.kweezy.singeditor.config.outbound.TypedOutbound;

import java.util.*;

public class ImportService {
    private final List<OutboundImporter> importers = new ArrayList<>();

    public ImportService() {
        // register default importers
        importers.add(new VlessImporter());
        importers.add(new ShadowsocksImporter());
    }

    public ImportResult importText(String text, SingBoxConfig cfg) {
        if (text == null) return new ImportResult(0, 0, List.of());
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

    private static String sanitizeTag(String base) {
        if (base == null) return null;
        String s = base.replaceAll("[^A-Za-z0-9]", "");
        return s.isEmpty() ? null : s;
    }

    private static String uniqueTag(String base, Set<String> existing) {
        String tag = base; int i = 2;
        while (existing.contains(tag)) tag = base + i++;
        return tag;
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
        } catch (Exception ignore) {
        }
    }
}
