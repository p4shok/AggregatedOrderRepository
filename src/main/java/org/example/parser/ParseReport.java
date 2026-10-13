package org.example.parser;

import org.example.model.TestResult;

import java.util.List;

public record ParseReport(List<TestResult> results, List<ParseReport> error) {
    public boolean hasErrors() {
        return !error.isEmpty();
    }
}
