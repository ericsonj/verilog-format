package net.ericsonj.verilog.elements;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Mar 6, 2019 12:18:35 AM
 */
public class ModuleDefinition {

    private int startIdxLine;
    private int endIdxLine;
    private int type;

    public ModuleDefinition(int type) {
        this.type = type;
    }

    public int getStartIdxLine() {
        return startIdxLine;
    }

    public void setStartIdxLine(int startIdxLine) {
        this.startIdxLine = startIdxLine;
    }

    public int getEndIdxLine() {
        return endIdxLine;
    }

    public void setEndIdxLine(int endIdxLine) {
        this.endIdxLine = endIdxLine;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ModuleDefinition{" + "startIdxLine=" + startIdxLine + ", endIdxLine=" + endIdxLine + ", type=" + type + '}';
    }

}
