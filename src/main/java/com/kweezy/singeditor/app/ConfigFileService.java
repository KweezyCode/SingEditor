package com.kweezy.singeditor.app;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kweezy.singeditor.config.SingBoxConfig;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class ConfigFileService {
    private final ObjectMapper mapper;

    public ConfigFileService(ObjectMapper mapper) {
        this.mapper = Objects.requireNonNull(mapper, "mapper");
    }

    public static ConfigFileService defaultService() {
        return new ConfigFileService(defaultMapper());
    }

    public Result<SingBoxConfig> load(File file) {
        Objects.requireNonNull(file, "file");
        try {
            SingBoxConfig cfg = mapper.readValue(file, SingBoxConfig.class);
            return Result.ok(cfg);
        } catch (IOException e) {
            return Result.fail("Error reading or parsing file: " + e.getMessage(), e);
        }
    }

    public Result<Void> save(File file, SingBoxConfig config) {
        Objects.requireNonNull(file, "file");
        Objects.requireNonNull(config, "config");
        try {
            mapper.writeValue(file, config);
            return Result.ok();
        } catch (IOException e) {
            return Result.fail("Error saving file: " + e.getMessage(), e);
        }
    }

    private static ObjectMapper defaultMapper() {
        ObjectMapper m = new ObjectMapper();
        m.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        m.enable(SerializationFeature.INDENT_OUTPUT);
        return m;
    }
}
