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
    public String decorateLine(FileFormat format, String line, int lineIndex) {
        int spaces = format.getSpacesBeforeIfStatement();
        String aux = line;
        if (line.matches(".*[ |\t]+if\\b[ ]*[(].*")
                || line.matches(".*[ |\t]+else\\b[ ]+?")
                || line.matches(".*else[ ]+if\\b[ ]*[(].*")) {
            aux = line.replaceAll("\\bif\\b[ ]*", "if" + StringHelper.getSpaces(spaces));
            aux = aux.replaceAll("\\belse\\b[ ]*", "else" + StringHelper.getSpaces(spaces));
        }
        return aux;
    }

    @Override
    public boolean inBlockComment() {
        return false;
    }

}
