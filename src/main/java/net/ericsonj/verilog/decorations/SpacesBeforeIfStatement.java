package net.ericsonj.verilog.decorations;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 28, 2019 11:46:28 PM
 */
public class SpacesBeforeIfStatement extends AbstractLineDecoration {

    @Override
    public String decorateLine(FileFormat format, String line) {
        int spaces = format.getSpacesBeforeIfStatement();
        String aux = line.replaceAll("if[ ]*", "if" + StringHelper.getSpaces(spaces));
        aux = aux.replaceAll("else[ ]*", "else" + StringHelper.getSpaces(spaces));
        return aux;
    }

    @Override
    public boolean inBlockComment() {
        return false;
    }

}
