package net.ericsonj.verilog.decorations;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.StyleImp;
import net.ericsonj.verilog.VerilogHelper;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class ModuleInstantiation implements StyleImp {

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {
        for (String line : buffer) {
            Pattern p = Pattern.compile("[ ]*([A-Za-z_-]+)[ ]+.*");
            Matcher m = p.matcher(line);
            if (m.find()) {
                String word = m.group(1);
                if (!VerilogHelper.isKeyWord(word)) {
                    ModuleAlign align = new ModuleAlign(word);
                    align.applyStyle(format, buffer);
                }
            }
        }
    }

}
