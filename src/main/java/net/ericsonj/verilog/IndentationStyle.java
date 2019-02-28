package net.ericsonj.verilog;

import java.util.LinkedList;
import net.ericsonj.verilog.statements.Always;
import net.ericsonj.verilog.statements.BlockComment;
import net.ericsonj.verilog.statements.Case;
import net.ericsonj.verilog.statements.For;
import net.ericsonj.verilog.statements.Forever;
import net.ericsonj.verilog.statements.Function;
import net.ericsonj.verilog.statements.If;
import net.ericsonj.verilog.statements.Initial;
import net.ericsonj.verilog.statements.Module;
import net.ericsonj.verilog.statements.Repeat;
import net.ericsonj.verilog.statements.Task;
import net.ericsonj.verilog.statements.While;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class IndentationStyle implements StyleImp {

    private LinkedList<LineIndentable> indents;

    public IndentationStyle() {
        this.indents = new LinkedList<>();

        this.indents.add(new BlockComment());
        this.indents.add(new For());
        this.indents.add(new While());
        this.indents.add(new Repeat());
        this.indents.add(new Forever());
        this.indents.add(new Case());
        this.indents.add(new If());
        this.indents.add(new Task());
        this.indents.add(new Function());
        this.indents.add(new Initial());
        this.indents.add(new Always());
        this.indents.add(new Module());
    }

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {
        for (int index = 0; index < buffer.size(); index++) {
            String line = buffer.get(index);
            String newLine = processLine(format, line);
            buffer.remove(index);
            buffer.add(index, newLine);
        }
    }

    private String processLine(FileFormat format, String line) {

        int recursive = 0;
        if (format.states.isEmpty() || format.states.size() == 1) {
            recursive = 1;
        } else {
            recursive = format.states.size();
        }

        for (int i = 0; i < recursive; i++) {
            for (LineIndentable indent : indents) {
                String indentLine = indent.indentLine(format, line.trim());
                if (indentLine != null) {
                    return indentLine;
                }
            }
        }

        return indent(format, line);
    }

    private String indent(FileFormat format, String line) {
        StringBuilder sb = new StringBuilder(line);
        for (int i = 0; i < (format.getIndentSize() * format.getCountIndent()); i++) {
            sb.insert(0, format.getIndentType());
        }
        return sb.toString();
    }

}
