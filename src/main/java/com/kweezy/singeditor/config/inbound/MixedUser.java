package com.kweezy.singeditor.config.inbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

/** Пользователь для Mixed inbound (SOCKS/HTTP). */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MixedUser {
    private String username;
    private String password;
}
