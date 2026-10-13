package org.example.parser;

import org.example.exception.ParseError;
import org.example.model.TestResult;

import java.util.List;

public record ParseReport(List<TestResult> results, List<ParseError> error) {
    public boolean hasErrors() {
        return !error.isEmpty();
    }
}
