package net.ericsonj.verilog.decorations;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author ericson
 */
public class SpacesBlockingAssignment extends AbstractLineDecoration {

    @Override
    public String decorateLine(FileFormat format, String line, int lineIndex) {
        if (format.getSpacesBlockingAssignment() == 0) {
            return line;
        }
        if (line.matches(".*[^<]=.*")) {
            String aux = line.replaceAll("[ ]*[^=!<>&|~\\^+\\-*/]=[ ]*", StringHelper.getSpaces(format.getSpacesBlockingAssignment()) + "=" + StringHelper.getSpaces(format.getSpacesBlockingAssignment()));
            return aux;
        }
        return line;
    }

    @Override
    public boolean inBlockComment() {
        return false;
    }

}
