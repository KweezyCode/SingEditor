package com.kweezy.singeditor.config.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.io.IOException;

/** Boolean | object union for udp_over_tcp. */
@JsonDeserialize(using = UdpOverTcp.Deserializer.class)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UdpOverTcp {
    private Boolean enabled;
    private Integer version;   // default 2

    public static final class Deserializer extends JsonDeserializer<UdpOverTcp> {
        @Override public UdpOverTcp deserialize(JsonParser p, DeserializationContext c) throws IOException {
            JsonNode n = p.readValueAsTree();
            if (n.isBoolean()) {
                boolean b = n.booleanValue();
                return UdpOverTcp.builder().enabled(b).version(b ? 2 : null).build();
            }
            return UdpOverTcp.builder()
                    .enabled(n.path("enabled").asBoolean(true))
                    .version(n.path("version").isInt() ? n.get("version").asInt() : 2)
                    .build();
        }
    }
}
