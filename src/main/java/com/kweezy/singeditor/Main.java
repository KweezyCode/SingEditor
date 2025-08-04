package com.kweezy.singeditor;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kweezy.singeditor.config.SingBoxConfig;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ObjectMapper om = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SingBoxConfig cfg = om.readValue(new File("config.json"), SingBoxConfig.class);
        System.out.println(cfg.getOutbounds().get(0).getClass()); // → ShadowsocksOutbound и т.д.

    }
}
