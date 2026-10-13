package org.example.error;

public record ParseError(int lineNumber, String rawLine, String reason) {

    @Override
    public String toString() {
        return "Строка: " + lineNumber + ": " + reason + " -> \"" + rawLine + "\"";
    }
}
