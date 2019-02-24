package net.ericsonj.verilog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class VerilogFile {

    public LinkedList<String> memFile;
    public LinkedList<StyleImp> styles;
    public final FileFormat format;
    public String pathname;

    public VerilogFile(String pathname, FileFormat format) {
        this.memFile = new LinkedList<>();
        this.styles = new LinkedList<>();
        this.format = format;
        this.pathname = pathname;
        try (FileReader fileReader = new FileReader(new File(pathname));
                BufferedReader bufferReader = new BufferedReader(fileReader)) {
            String line;
            while ((line = bufferReader.readLine()) != null) {
                memFile.add(line.trim());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VerilogFormat.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VerilogFormat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void format() {
        for (StyleImp style : styles) {
            style.applyStyle(format, memFile);
        }
    }

    public void addStyle(StyleImp style) {
        this.styles.add(style);
    }

    public void print() {
        for (String line : memFile) {
            System.out.println(line);
        }
    }

    public void overWrite() {

        try {
            FileWriter formatFile = new FileWriter(new File(pathname), false);
            for (String string : memFile) {
                formatFile.write(string + FileFormat.LF);
            }
            formatFile.close();
        } catch (IOException ex) {
            Logger.getLogger(VerilogFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
