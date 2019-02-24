package net.ericsonj.commonscli;

import java.util.Properties;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 *
 * @author ejoseph
 */
public class CommonsCLI extends ConsoleApplication {

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        CommonsCLI cli = new CommonsCLI();
//        cli.process(args);
//    }

    @Override
    public Options getOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, "print this message");
        options.addOption("a", "all", false, "do not hide entries starting with .");
        options.addOption("A", "almost-all", false, "do not list implied . and ..");
        options.addOption("b", "escape", false, "print octal escapes for nongraphic "
                + "characters");

        options.addOption(Option.builder()
                .longOpt("block-size")
                .desc("use SIZE-byte blocks")
                .hasArg()
                .build());

        options.addOption(Option.builder("D").argName("property=value")
                .hasArgs()
                .valueSeparator()
                .desc("use value for given property")
                .build());

        options.addOption(Option.builder("logfile")
                .argName("file")
                .hasArg()
                .desc("use given file for log").build());

        options.addOption("B", "ignore-backups", false, "do not list implied entried "
                + "ending with ~");

        options.addOption("c", false, "with -lt: sort by, and show, ctime (time of last "
                + "modification of file status information) with "
                + "-l:show ctime and sort by name otherwise: sort "
                + "by ctime");

        options.addOption("C", false, "list entries by columns");

        return options;
    }

    @Override
    public void processOptions(CommandLine line) {

        if (isArgsEmpty()) {
            printHelp();
            return;
        }

        if (line.hasOption("block-size")) {
            System.out.println(line.getOptionValue("block-size"));
        }

        if (line.hasOption("a")) {
            System.out.println("all: " + line.getOptionValue("a"));
        }

        if (line.hasOption("D")) {
            Properties p = line.getOptionProperties("D");
            System.out.println("D: property2 = |" + p.getProperty("property2") + "|");
            System.out.println("D: property1 = |" + p.getProperty("property1") + "|");
        }

        if (line.hasOption("logfile")) {
            System.out.println("logfile: |" + line.getOptionValue("logfile") + "|");
        }

        if (line.hasOption("help")) {
            printHelp();
        }

    }

}
