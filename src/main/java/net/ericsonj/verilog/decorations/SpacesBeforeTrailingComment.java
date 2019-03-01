package net.ericsonj.verilog.decorations;

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
            if (line.matches("[ ]*//.*")) {
                return line.replaceAll("//[ ]*", "//" + getSpaces(format.getSpacesBeforeTrailingComments()));
            }
        }
        return line;
    }
    
    private String getSpaces(int count) {
        String spaces = "";
        for (int i = 0; i < count; i++) {
            spaces += " ";
        }
        return spaces;
    }
    
}
