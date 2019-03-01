package net.ericsonj.verilog.decorations;

import java.util.LinkedList;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author ericson
 */
public class AlignLineComment extends AlignConsecutive {

    public AlignLineComment() {
        super(".*", "//", ".*");
    }

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {
        if (format.getAlignLineComments()) {
            super.applyStyle(format, buffer); //To change body of generated methods, choose Tools | Templates.
        }
    }

}
