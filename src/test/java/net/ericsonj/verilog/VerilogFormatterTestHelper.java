package net.ericsonj.verilog;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.ericsonj.verilog.decorations.AlignBlockingAssignments;
import net.ericsonj.verilog.decorations.AlignLineComment;
import net.ericsonj.verilog.decorations.AlignNoBlockingAssignments;
import net.ericsonj.verilog.decorations.ModuleAlign;
import net.ericsonj.verilog.decorations.ModuleInstantiation;
import net.ericsonj.verilog.decorations.SpacesBeforeIfStatement;
import net.ericsonj.verilog.decorations.SpacesBlockingAssignment;
import net.ericsonj.verilog.decorations.SpacesInParentheses;
import net.ericsonj.verilog.decorations.SpacesInSquareBrackets;
import net.ericsonj.verilog.decorations.SpacesNoBlockingAssignment;
import net.ericsonj.verilog.decorations.SpacesTrailingComment;

final class VerilogFormatterTestHelper {

    private VerilogFormatterTestHelper() {
    }

    static LinkedList<String> formatLines(String... lines) {
        return formatLines(new FormatSetting(null), Arrays.asList(lines));
    }

    static LinkedList<String> formatLines(File settingsFile, String... lines) {
        return formatLines(new FormatSetting(settingsFile), Arrays.asList(lines));
    }

    static LinkedList<String> formatLines(FormatSetting settings, List<String> lines) {
        LinkedList<String> buffer = new LinkedList<>(lines);
        FileFormat format = new FileFormat(settings);
        applyDefaultStyles(format, buffer);
        return buffer;
    }

    static VerilogFile formatFile(String pathname) {
        return formatFile(pathname, null);
    }

    static VerilogFile formatFile(String pathname, File settingsFile) {
        FileFormat format = new FileFormat(new FormatSetting(settingsFile));
        VerilogFile file = new VerilogFile(pathname, format);
        file.addStyle(new NormalizeBeginEndStyle());
        file.addStyle(new IndentationStyle());
        file.addStyle(new ModuleAlign());
        file.addStyle(new ModuleInstantiation());
        file.addStyle(new SpacesTrailingComment());
        file.addStyle(new SpacesBeforeIfStatement());
        file.addStyle(new SpacesBlockingAssignment());
        file.addStyle(new SpacesNoBlockingAssignment());
        file.addStyle(new SpacesInParentheses());
        file.addStyle(new SpacesInSquareBrackets());
        file.addStyle(new AlignBlockingAssignments());
        file.addStyle(new AlignNoBlockingAssignments());
        file.addStyle(new AlignLineComment());
        file.format();
        return file;
    }

    private static void applyDefaultStyles(FileFormat format, LinkedList<String> buffer) {
        new NormalizeBeginEndStyle().applyStyle(format, buffer);
        new IndentationStyle().applyStyle(format, buffer);
        new ModuleAlign().applyStyle(format, buffer);
        new ModuleInstantiation().applyStyle(format, buffer);
        new SpacesTrailingComment().applyStyle(format, buffer);
        new SpacesBeforeIfStatement().applyStyle(format, buffer);
        new SpacesBlockingAssignment().applyStyle(format, buffer);
        new SpacesNoBlockingAssignment().applyStyle(format, buffer);
        new SpacesInParentheses().applyStyle(format, buffer);
        new SpacesInSquareBrackets().applyStyle(format, buffer);
        new AlignBlockingAssignments().applyStyle(format, buffer);
        new AlignNoBlockingAssignments().applyStyle(format, buffer);
        new AlignLineComment().applyStyle(format, buffer);
    }
}
