package com.kweezy.singeditor.importer;

import com.kweezy.singeditor.config.common.TlsOutboundConfig;
import com.kweezy.singeditor.config.common.WsTransport;
import com.kweezy.singeditor.config.outbound.VlessOutbound;
import com.kweezy.singeditor.config.outbound.TypedOutbound;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VlessImporter implements OutboundImporter {
    @Override
    public boolean supports(String line) {
        return line != null && line.trim().toLowerCase(Locale.ROOT).startsWith("vless://");
    }

    @Override
    public Optional<TypedOutbound> parse(String line) {
        try {
            VlessOutbound v = parseVlessUri(line.trim());
            return Optional.ofNullable(v);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private VlessOutbound parseVlessUri(String uri) {
        String noScheme = uri.substring("vless://".length());
        int hash = noScheme.indexOf('#');
        if (hash >= 0) {
            noScheme = noScheme.substring(0, hash);
        }
        int at = noScheme.indexOf('@');
        if (at < 0) return null;
        String userinfo = noScheme.substring(0, at);
        String rest = noScheme.substring(at + 1);

        String authority;
        String query = null;
        int q = rest.indexOf('?');
        if (q >= 0) {
            authority = rest.substring(0, q);
            query = rest.substring(q + 1);
        } else {
            authority = rest;
        }
        String host;
        Integer port = null;
        int colon = authority.lastIndexOf(':');
        if (colon > 0) {
            host = authority.substring(0, colon);
            try { port = Integer.parseInt(authority.substring(colon + 1)); } catch (NumberFormatException ignore) {}
        } else {
            host = authority;
        }
        Map<String, String> params = parseQuery(query);

        VlessOutbound ob = new VlessOutbound();
        ob.setServer(host);
        ob.setServerPort(port);
        ob.setUuid(userinfo);

        String flow = params.get("flow");
        if (flow != null && !flow.isEmpty()) ob.setFlow(flow);

        // TLS / Reality / UTLS
        String security = params.get("security");
        if (security != null && (security.equalsIgnoreCase("reality") || security.equalsIgnoreCase("tls"))) {
            TlsOutboundConfig tls = new TlsOutboundConfig();
            tls.setEnabled(true);
            String sni = params.get("sni");
            if (sni != null && !sni.isEmpty()) tls.setServerName(sni);
            if (security.equalsIgnoreCase("reality")) {
                TlsOutboundConfig.RealityConfigOutbound reality = new TlsOutboundConfig.RealityConfigOutbound();
                reality.setEnabled(true);
                String pbk = params.get("pbk");
                if (pbk != null) reality.setPublicKey(pbk);
                String sid = params.getOrDefault("sid", params.get("short_id"));
                if (sid != null) reality.setShortId(sid);
                tls.setReality(reality);

                TlsOutboundConfig.UtlsConfig utls = new TlsOutboundConfig.UtlsConfig();
                utls.setEnabled(true);
                tls.setUtls(utls);
                String fp = params.get("fp");
                if (fp != null && !fp.isEmpty()) {
                    utls.setFingerprint(fp);
                }
                else {
                    utls.setFingerprint("random");
                }
            }
            ob.setTls(tls);
        }

        // Transport (ws)
        String type = params.get("type");
        if (type != null && type.equalsIgnoreCase("ws")) {
            WsTransport ws = new WsTransport();
            String path = params.get("path");
            if (path != null) ws.setPath(path);
            String hostHeader = params.get("host");
            if (hostHeader != null && !hostHeader.isEmpty()) {
                Map<String, String> headers = new LinkedHashMap<>();
                headers.put("Host", hostHeader);
                ws.setHeaders(headers);
            }
            ob.setTransport(ws);
        }
        return ob;
    }

    private static Map<String, String> parseQuery(String q) {
        Map<String, String> map = new LinkedHashMap<>();
        if (q == null || q.isEmpty()) return map;
        String[] pairs = q.split("&");
        for (String p : pairs) {
            if (p.isEmpty()) continue;
            int eq = p.indexOf('=');
            String k = eq >= 0 ? p.substring(0, eq) : p;
            String v = eq >= 0 ? p.substring(eq + 1) : "";
            k = URLDecoder.decode(k, StandardCharsets.UTF_8);
            v = URLDecoder.decode(v, StandardCharsets.UTF_8);
            map.put(k, v);
        }
        return map;
    }
}
