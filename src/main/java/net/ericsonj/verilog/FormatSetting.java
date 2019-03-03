package net.ericsonj.verilog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 17, 2019 11:12:50 PM
 */
public class FormatSetting {

    public static final String FILE_PROP = "verilog-format.properties";

    private File file;
    private Properties prop;
    private boolean filePresent;

    public FormatSetting() {
        this(new File(FILE_PROP));
    }

    public FormatSetting(File file) {
        this.file = file;
        this.prop = new Properties();
        if (this.file == null) {
            this.filePresent = false;
            return;
        }
        if (!this.file.exists()) {
            this.filePresent = false;
            return;
        }
        try {
            this.prop.load(new FileReader(file));
            this.filePresent = true;
            return;
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        this.filePresent = false;
    }

    public String getStringValue(String key, String defaultValue) {
        return prop.getProperty(key, defaultValue);
    }

    public int getIntValue(String key, int defaultValue) {
        int value = Integer.parseInt(prop.getProperty(key, String.valueOf(defaultValue)));
        return value;
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        String value = getStringValue(key, String.valueOf(defaultValue));
        return Boolean.valueOf(value);
    }

}
