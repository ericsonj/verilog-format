package net.ericsonj.verilog.decorations;

import java.util.LinkedList;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author ericson
 */
public class AlignNoBlockingAssignments extends AlignConsecutive {

    public AlignNoBlockingAssignments() {
        super(".*", "<=", ".*[;]");
    }

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {
        if (format.getAlignNoBlockingAssignments()) {
            super.applyStyle(format, buffer); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
