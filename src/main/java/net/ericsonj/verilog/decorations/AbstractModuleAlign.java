package net.ericsonj.verilog.decorations;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.StyleImp;
import net.ericsonj.verilog.VerilogHelper;
import net.ericsonj.verilog.elements.ModuleDefinition;

/**
 *
 * @author ejoseph
 */
public abstract class AbstractModuleAlign implements StyleImp {

    private enum STATE {
        INIT,
        IN_COMMENT,
        IN_MODULE,
        IN_GEN_MODULE
    }

    private STATE state = STATE.INIT;
    private LinkedList<ModuleDefinition> modulesDef = new LinkedList<>();

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {

        int startIdx = 0;

        for (int i = 0; i < buffer.size(); i++) {
            String line = buffer.get(i);
            switch (state) {
                case INIT:
                    if (isLineComment(line)) {
                    } else if (isStartBlockComment(line)) {
                        state = STATE.IN_COMMENT;
                    } else if (isLineModule(line) || isLineGenModule(line)) {
                        state = STATE.INIT;
                        ModuleDefinition mod = new ModuleDefinition(isLineModule(line) ? 1 : 2);
                        mod.setStartIdxLine(i);
                        mod.setEndIdxLine(i);
                        modulesDef.add(mod);
                    } else if (isModule(line)) {
                        state = STATE.IN_MODULE;
                        startIdx = i;
                    } else if (isGenModule(line)) {
                        state = STATE.IN_GEN_MODULE;
                        startIdx = i;
                    } else {

                    }
                    break;
                case IN_COMMENT:
                    if (isEndBlockComment(line)) {
                        state = STATE.INIT;
                    }
                    break;
                case IN_MODULE:
                    if (isEndModule(line)) {
                        state = STATE.INIT;
                        ModuleDefinition mod = new ModuleDefinition(1);
                        mod.setStartIdxLine(startIdx);
                        mod.setEndIdxLine(i);
                        modulesDef.add(mod);
                    }
                    break;
                case IN_GEN_MODULE:
                    if (isEndGenModule(line)) {
                        ModuleDefinition mod = new ModuleDefinition(2);
                        mod.setStartIdxLine(startIdx);
                        mod.setEndIdxLine(i);
                        modulesDef.add(mod);
                        state = STATE.INIT;
                    }
                    break;
                default:
                    throw new AssertionError(state.name());
            }
        }

        alignModules(format, buffer, modulesDef);
        
    }

    public abstract void alignModules(FileFormat format, LinkedList<String> buffer, LinkedList<ModuleDefinition> modules);

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

    private boolean isLineModule(String line) {
        return line.matches("^[ ]*module[ ]+.*[;].*");
    }

    private boolean isEndModule(String line) {
        return line.matches(".*[;].*");
    }

    private boolean isGenModule(String line) {
        Pattern p = Pattern.compile("^[ ]*([A-Za-z0-9_-]+)[ ]+?.*");
        Matcher m = p.matcher(line);
        if (m.find()) {
            String name = m.group(1);
            if (!VerilogHelper.isKeyWord(name) && !isVarAssign(line)) {
                return true;
            }
        }
        p = Pattern.compile("^[ ]*([A-Za-z0-9_-]+)$");
        m = p.matcher(line);
        if (m.find()) {
            String name = m.group(1);
            if (!VerilogHelper.isKeyWord(name) && !isVarAssign(line)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLineGenModule(String line) {
        return line.matches(".*[;].*") && isGenModule(line);
    }

    private boolean isEndGenModule(String line) {
        return line.matches(".*[;].*");
    }

    private boolean isVarAssign(String line) {
        return line.contains("=") && line.contains(";");
    }

}
