package net.ericsonj.verilog.decorations;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.StyleImp;
import net.ericsonj.verilog.VerilogFile;
import net.ericsonj.verilog.VerilogHelper;

/**
 *
 * @author ejoseph
 */
public class AbstractModuleAlign implements StyleImp {

    private enum STATE {
        INIT,
        IN_COMMENT,
        IN_MODULE,
        IN_GEN_MODULE
    }

    private STATE state = STATE.INIT;

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {
        for (int i = 0; i < buffer.size(); i++) {
            String line = buffer.get(i);
            switch (state) {
                case INIT:
                    if (isLineComment(line)) {
                    } else if (isStartBlockComment(line)) {
                        state = STATE.IN_COMMENT;
                    } else if (isModule(line)) {
                        state = STATE.IN_MODULE;
                        System.out.println(line);
                    } else if (isGenModule(line)) {
                        state = STATE.IN_GEN_MODULE;
                    } else {

                    }
                    break;
                case IN_COMMENT:
                    if (isEndBlockComment(line)) {
                        state = STATE.INIT;
                    }
                    break;
                case IN_MODULE:
                    System.out.println(line);
                    if (isEndModule(line)) {
                        state = STATE.INIT;
                        System.out.println("-------");
                    }
                    break;
                case IN_GEN_MODULE:

                    break;
                default:
                    throw new AssertionError(state.name());

            }
        }
    }

    private boolean isLineComment(String line) {
        return line.matches("^[ ]*//.*");
    }

    private boolean isStartBlockComment(String line) {
        return line.matches("^[ ]*/\\*.*");
    }

    private boolean isEndBlockComment(String line) {
        return line.matches(".*\\*/.*");
    }

    private boolean isModule(String line) {
        return line.matches("^[ ]*module[ ]+.*");
    }

    private boolean isEndModule(String line) {
        return line.matches(".*[;].*");
    }

    private boolean isGenModule(String line) {
        Pattern p = Pattern.compile("^[ ]*([A-Za-z0-9_-])[ ]+.*");
        Matcher m = p.matcher(line);
        if (m.find()) {
            String name = m.group(1);
            if (!VerilogHelper.isKeyWord(name)) {
                return true;
            }
        }
        return false;
    }

}
