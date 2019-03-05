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

            if (line.matches("^[ ]*\\.*") || line.matches("^[ ]*/\\*.*")) {
                continue;
            }

            Pattern p = Pattern.compile("^[ ]*([0-9A-Za-z-_]+)[ ]+.*");
            Matcher m = p.matcher(line);
            if (m.find()) {
                String word = m.group(1);
                boolean isModuelInst = (line.matches(".*[ ]+[(].*") || line.matches(".*" + word + "[ ]+[#][(].*"));
                if (!VerilogHelper.isKeyWord(word) && isModuelInst) {
//                    System.out.println(line);
                    ModuleAlign align = new ModuleAlign(word);
                    align.applyStyle(format, buffer);
                    break;
                }
            }

        }
    }

}
