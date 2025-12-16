package com.kweezy.singeditor.importer;

import com.kweezy.singeditor.config.outbound.ShadowsocksOutbound;
import com.kweezy.singeditor.config.outbound.TypedOutbound;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

public class ShadowsocksImporter implements OutboundImporter {
    @Override
    public boolean supports(String line) {
        return line.startsWith("ss://");
    }

    @Override
    public Optional<TypedOutbound> parse(String line) throws Exception {
        // Format: ss://BASE64(method:password)@host:port#tag
        // Or: ss://BASE64(method:password@host:port)#tag (legacy)
        // We focus on the first one as per example, but let's try to be robust.
        
        String content = line.substring(5);
        String tag = "";
        int hashIdx = content.indexOf('#');
        if (hashIdx != -1) {
            tag = content.substring(hashIdx + 1);
            content = content.substring(0, hashIdx);
            try {
                tag = URLDecoder.decode(tag, StandardCharsets.UTF_8);
            } catch (Exception ignored) {}
        }

        // Check for @
        int atIdx = content.lastIndexOf('@');
        String userInfo;
        String hostPort;
        
        if (atIdx != -1) {
            // Format: userInfo@host:port
            userInfo = content.substring(0, atIdx);
            hostPort = content.substring(atIdx + 1);
        } else {
            // Maybe everything is base64 encoded?
            // Legacy format: ss://BASE64(method:password@host:port)
            try {
                String decoded = new String(Base64.getUrlDecoder().decode(content), StandardCharsets.UTF_8);
                int at = decoded.lastIndexOf('@');
                if (at != -1) {
                    userInfo = decoded.substring(0, at);
                    hostPort = decoded.substring(at + 1);
                } else {
                    // Invalid format
                    return Optional.empty();
                }
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }

        // Decode userInfo if it looks like base64 (no colon)
        // Standard SIP002: userinfo = base64(method:password)
        if (!userInfo.contains(":")) {
            try {
                userInfo = new String(Base64.getUrlDecoder().decode(userInfo), StandardCharsets.UTF_8);
            } catch (IllegalArgumentException e) {
                // maybe it wasn't base64?
            }
        }

        String[] parts = userInfo.split(":", 2);
        if (parts.length != 2) return Optional.empty();
        String method = parts[0];
        String password = parts[1];

        // Parse host:port
        String server;
        int serverPort;
        int colon = hostPort.lastIndexOf(':');
        if (colon != -1) {
            server = hostPort.substring(0, colon);
            try {
                serverPort = Integer.parseInt(hostPort.substring(colon + 1));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        } else {
            return Optional.empty();
        }

        ShadowsocksOutbound out = ShadowsocksOutbound.builder()
                .tag(tag.isEmpty() ? "ss" : tag)
                .server(server)
                .serverPort(serverPort)
                .method(method)
                .password(password)
                .build();
        
        return Optional.of(out);
    }
}
