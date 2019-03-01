package net.ericsonj.verilog.decorations;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author ericson
 */
public class SpacesNoBlockingAssignment extends AbstractLineDecoration {

    @Override
    public String decorateLine(FileFormat format, String line, int lineIndex) {
        if (format.getSpacesNoBlockingAssignment() == 0) {
            return line;
        }
        String aux = line.replaceAll("[ ]*<=[ ]*", StringHelper.getSpaces(format.getSpacesNoBlockingAssignment()) + "<=" + StringHelper.getSpaces(format.getSpacesNoBlockingAssignment()));
        return aux;
    }

    @Override
    public boolean inBlockComment() {
        return false;
    }

}
