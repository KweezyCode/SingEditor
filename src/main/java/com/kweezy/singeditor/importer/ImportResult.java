package com.kweezy.singeditor.importer;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ImportResult {
    private int imported;
    private int failed;
    private List<String> errors;
}

