package net.ericsonj.verilog.decorations;

import java.util.LinkedList;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author ericson
 */
public class AlignBlockingAssignments extends AlignConsecutive {

    public AlignBlockingAssignments() {
        super(".*[^<]", "=", ".*[;]");
    }

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {
        if (format.getAlignBlockingAssignments()) {
            super.applyStyle(format, buffer); //To change body of generated methods, choose Tools | Templates.
        }
    }

}
