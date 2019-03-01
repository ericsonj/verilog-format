package net.ericsonj.verilog.decorations;

import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author ericson
 */
public class SpacesInParentheses extends AbstractLineDecoration {

    @Override
    public String decorateLine(FileFormat format, String line, int lineIndex) {
        if (!line.matches(".*[(].*[)].*")) {
            return line;
        }
        if (format.getSpacesInParentheses()) {
            String aux = line.replaceAll("[(][ ]*", "( ");
            aux = aux.replaceAll("[ ]*[)]", " )");
            return aux;
        } else {
            String aux = line.replaceAll("[(][ ]*", "(");
            aux = aux.replaceAll("[ ]*[)]", ")");
            return aux;
        }
    }

    @Override
    public boolean inBlockComment() {
        return false;
    }

}
