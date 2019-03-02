package net.ericsonj.verilog;

import java.io.File;
import net.ericsonj.commonscli.ConsoleApplication;
import net.ericsonj.verilog.decorations.AlignBlockingAssignments;
import net.ericsonj.verilog.decorations.AlignLineComment;
import net.ericsonj.verilog.decorations.AlignNoBlockingAssignments;
import net.ericsonj.verilog.decorations.ModuleAlign;
import net.ericsonj.verilog.decorations.SpacesBeforeIfStatement;
import net.ericsonj.verilog.decorations.SpacesTrailingComment;
import net.ericsonj.verilog.decorations.SpacesBlockingAssignment;
import net.ericsonj.verilog.decorations.SpacesInParentheses;
import net.ericsonj.verilog.decorations.SpacesInSquareBrackets;
import net.ericsonj.verilog.decorations.SpacesNoBlockingAssignment;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class VerilogFormat extends ConsoleApplication {

    private boolean printFileFormated = false;
    private FormatSetting settings;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VerilogFormat vf = new VerilogFormat();
        vf.process(args);
    }

    @Override
    protected Options getOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "print this message");
        options.addOption("v", "version", false, "verilog-format version");
        options.addOption(Option.builder("f")
                .longOpt("format")
                .argName("pathname")
                .hasArg()
                .desc("Verilog file")
                .build());
        options.addOption("p", "print", false, "print file formated");
        options.addOption(Option.builder("s")
                .longOpt("settings")
                .argName("settings-file")
                .hasArg()
                .desc("Settings config")
                .build());
        return options;
    }

    @Override
    protected void processOptions(CommandLine line) {
        if (isArgsEmpty()) {
            printHelp();
            return;
        }

        if (line.hasOption("h")) {
            printHelp();
            return;
        }

        if (line.hasOption("v")) {
            System.out.println(Global.VERSION);
            return;
        }

        if (line.hasOption("p")) {
            printFileFormated = true;
        }

        if (line.hasOption("s")) {
            String pathname = line.getOptionValue("s");
            settings = new FormatSetting(new File(pathname));
        } else {

            File localSetting = new File(".verilog-format.properties");
            if (localSetting.exists()) {
                settings = new FormatSetting(localSetting);
            } else {
                settings = new FormatSetting(null);
            }

        }

        if (line.hasOption("f")) {
            String pathname = line.getOptionValue("f");
            File file = new File(pathname);
            if (!file.exists() || file.isDirectory()) {
                printError("File not found");
                System.exit(0);
                return;
            }
            formatFile(file);
        }
    }

    private void formatFile(File file) {
        FileFormat format = new FileFormat(this.settings);
        VerilogFile vFile = new VerilogFile(file.getAbsolutePath(), format);
        vFile.addStyle(new IndentationStyle());
        vFile.addStyle(new ModuleAlign());
        vFile.addStyle(new SpacesTrailingComment());
        vFile.addStyle(new SpacesBeforeIfStatement());
        vFile.addStyle(new SpacesBlockingAssignment());
        vFile.addStyle(new SpacesNoBlockingAssignment());
        vFile.addStyle(new SpacesInParentheses());
        vFile.addStyle(new SpacesInSquareBrackets());
        vFile.addStyle(new AlignBlockingAssignments());
        vFile.addStyle(new AlignNoBlockingAssignments());
        vFile.addStyle(new AlignLineComment());
        vFile.format();
        if (printFileFormated) {
            vFile.print();
        } else {
            vFile.overWrite();
        }
    }

    private void printError(String message) {
        System.err.println(message);
    }

    @Override
    protected String getApplicationName() {
        return "verilog-format"; //To change body of generated methods, choose Tools | Templates.
    }

}
