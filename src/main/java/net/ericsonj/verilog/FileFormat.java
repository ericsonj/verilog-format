package net.ericsonj.verilog;

import java.util.LinkedList;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class FileFormat {

    public static final String SPACES_BEFORE_TRAILING_COMMENTS = "SpacesBeforeTrailingComments";
    public static final String SPACES_BEFORE_IF_STATEMENT = "SpacesBeforeIfStatement";
    public static final char INDENT_TAB = '\t';
    public static final char INDENT_SPACE = ' ';
    public static final char LF = '\n';
    public static final String CRLF = "\r\n";

    private FormatSetting setting;

    public LinkedList<StatementState> states;

    private int countIndent;

    public FileFormat(FormatSetting setting) {
        this.setting = setting;
        this.countIndent = 0;
        this.states = new LinkedList<>();
    }

    public char getIndentType() {
        String value = this.setting.getStringValue("IndentType", "space");
        if (value.equals("tab")) {
            return INDENT_TAB;
        }
        return INDENT_SPACE;
    }

    public int getIndentSize() {
        return this.setting.getIntValue("IndentWidth", 4);
    }

    public void addCountIndent() {
        this.countIndent++;
    }

    public void resCountIndent() {
        this.countIndent--;
    }

    public int getCountIndent() {
        return countIndent;
    }

    public LinkedList<StatementState> getStates() {
        return states;
    }

    public void setCountIndent(int countIndent) {
        this.countIndent = countIndent;
    }

    public FormatSetting getSetting() {
        return setting;
    }

    public int getSpacesBeforeTrailingComments() {
        return this.setting.getIntValue(SPACES_BEFORE_TRAILING_COMMENTS, 1);
    }
    
    public int getSpacesBeforeIfStatement() {
        return this.setting.getIntValue(SPACES_BEFORE_IF_STATEMENT, 1);
    }

    
}
