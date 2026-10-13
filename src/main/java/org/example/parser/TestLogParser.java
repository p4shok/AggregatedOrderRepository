package org.example.parser;

import org.example.exception.ParseError;
import org.example.model.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestLogParser {
    private Status status;
    private TestResult testResult;

    public TestLogParser(Status status, TestResult testResult) {
        this.status = status;
        this.testResult = testResult;
    }

    public ParseReport parse(List<String> lines) {
        Map<String, TestResult> lastByTest = new LinkedHashMap<>();
        List<ParseError> errors = new ArrayList<>();

        int lineNumber = 0;
        for(String line : lines) {
            lineNumber++;

            try {
                TestResult result
            }
        }
    }
}
