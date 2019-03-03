package net.ericsonj.verilog.decorations;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author ejoseph
 */
public class SpacesTrailingComment extends AbstractLineDecoration {

    @Override
    public String decorateLine(FileFormat format, String line, int LineIndex) {
        if (format.getSpacesAfterTrailingComments() == 0) {
            return line;
        }

        String aux = line;
        if (line.matches("[ ]*//.*")) {
            if (format.getSpacesAfterTrailingComments() > 0) {
                aux = aux.replaceAll("//[ ]*", "//" + StringHelper.getSpaces(format.getSpacesAfterTrailingComments()));
            }
            return aux;
        }
        if (line.matches(".*[ ]+//.*")) {
            if (format.getSpacesAfterTrailingComments() > 0) {
                aux = aux.replaceAll("//[ ]*", "//" + StringHelper.getSpaces(format.getSpacesAfterTrailingComments()));
            }
            if (format.getSpacesBeforeTrailingComments() > 0) {
                aux = aux.replaceAll("[ ]*//", StringHelper.getSpaces(format.getSpacesBeforeTrailingComments()) + "//");
            }
        } else if (line.matches(".*[^ ]//.*")) {
            if (format.getSpacesAfterTrailingComments() > 0) {
                aux = aux.replaceAll("//[ ]*", " //" + StringHelper.getSpaces(format.getSpacesAfterTrailingComments()));
            }
            if (format.getSpacesBeforeTrailingComments() > 0) {
                aux = aux.replaceAll("//", StringHelper.getSpaces(format.getSpacesBeforeTrailingComments()) + "//");
            }
        }

        return aux;
    }

    @Override
    public boolean inBlockComment() {
        return false;
    }

}
