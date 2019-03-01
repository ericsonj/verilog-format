package net.ericsonj.verilog.decorations;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author ejoseph
 */
public class SpacesBeforeTrailingComment extends AbstractLineDecoration {

    @Override
    public String decorateLine(FileFormat format, String line) {
        if (format.getSpacesBeforeTrailingComments() == 0) {
            return line;
        } else {
            if (line.matches(".*[ ]+//.*")) {
                return line.replaceAll("//[ ]*", "//" + StringHelper.getSpaces(format.getSpacesBeforeTrailingComments()));
            } else if (line.matches(".*[^ ]//.*")) {
                return line.replaceAll("//[ ]*", " //" + StringHelper.getSpaces(format.getSpacesBeforeTrailingComments()));
            }
        }
        return line;
    }

    @Override
    public boolean inBlockComment() {
        return false;
    }

}
