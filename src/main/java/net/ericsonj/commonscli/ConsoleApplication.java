package net.ericsonj.commonscli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author ejoseph
 */
public abstract class ConsoleApplication {

    private final CommandLineParser parser;
    private Options options;
    private String[] args;

    public ConsoleApplication() {
        this.parser = new DefaultParser();
        this.options = new Options();
    }

    protected abstract Options getOptions();

    protected abstract void processOptions(CommandLine line);

    protected  String getApplicationName(){
        return getClass().getSimpleName();
    }

    public void process(String[] args) {
        this.args = args;
        this.options = getOptions();
        try {
            CommandLine line = parser.parse(options, args);
            processOptions(line);
        } catch (ParseException ex) {
//            Logger.getLogger(ConsoleApplication.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
            printHelp();
        }
    }

    /**
     * Print in system out Help. 
     */
    protected void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(getCmdLineSyntax(),getHeaderHelp(),options,getFooterHelp(),true);
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public boolean isArgsEmpty() {
        return (this.args.length == 0);
    }
    
    public String getCmdLineSyntax(){
        return "java -jar "+ getApplicationName()+".jar";
    }
    
    public String getHeaderHelp(){
        return "";
    }
    
    public String getFooterHelp(){
        return "";
    }
}
