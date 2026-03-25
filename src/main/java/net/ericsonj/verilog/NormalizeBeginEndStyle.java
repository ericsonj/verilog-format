package net.ericsonj.verilog;

import java.util.LinkedList;

/**
 * Normalizes tightly-coupled begin/end tokens so the indentation state machines
 * can treat them as standalone Verilog keywords.
 */
public class NormalizeBeginEndStyle implements StyleImp {

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {
        for (int index = 0; index < buffer.size(); index++) {
            String line = normalizeInlineBegin(buffer.get(index));
            line = normalizeInlineEndElse(line);
            buffer.set(index, line);

            if (hasInlineEnd(line)) {
                int splitIndex = line.lastIndexOf(';');
                String statement = line.substring(0, splitIndex + 1).trim();
                String end = line.substring(splitIndex + 1).trim();

                buffer.set(index, statement);
                buffer.add(index + 1, end);
            }
        }
    }

    private String normalizeInlineBegin(String line) {
        String normalized = line;
        normalized = normalized.replaceAll("\\)begin\\b", ") begin");
        normalized = normalized.replaceAll("\\belsebegin\\b", "else begin");
        return normalized;
    }

    private String normalizeInlineEndElse(String line) {
        String normalized = line;
        normalized = normalized.replaceAll("\\bendelse\\b", "end else");
        normalized = normalized.replaceAll("\\bend\\s+elsebegin\\b", "end else begin");
        return normalized;
    }

    private boolean hasInlineEnd(String line) {
        String trimmed = line.trim();
        return trimmed.matches(".*;\\s*end\\b.*") && !trimmed.matches("^\\bend\\b.*");
    }
}
