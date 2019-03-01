package net.ericsonj.verilog;

import java.util.LinkedList;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class FileFormat {

    public static final String SPACES_BEFORE_TRAILING_COMMENTS = "SpacesBeforeTrailingComments";
    public static final String SPACES_BEFORE_IF_STATEMENT = "SpacesBeforeIfStatement";
    public static final String SPACES_BLOKING_ASSIGNMENTS = "SpacesBlockingAssignment";
    public static final String SPACES_NOBLOKING_ASSIGNMENTS = "SpacesNoBlockingAssignment";
    public static final String SPACES_IN_PARENTHESES = "SpacesInParentheses";
    public static final String ALIGN_BLOKING_ASSIGNMENTS = "AlignBlockingAssignments";
    public static final String ALIGN_NOBLOKING_ASSIGNMENTS = "AlignNoBlockingAssignments";
    public static final String ALIGN_LINE_COMMENTS = "AlignLineComments";

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

    public int getSpacesBlockingAssignment() {
        return this.setting.getIntValue(SPACES_BLOKING_ASSIGNMENTS, 1);
    }

    public int getSpacesNoBlockingAssignment() {
        return this.setting.getIntValue(SPACES_NOBLOKING_ASSIGNMENTS, 1);
    }

    public boolean getAlignBlockingAssignments() {
        return this.setting.getBooleanValue(ALIGN_BLOKING_ASSIGNMENTS, true);
    }

    public boolean getAlignNoBlockingAssignments() {
        return this.setting.getBooleanValue(ALIGN_NOBLOKING_ASSIGNMENTS, true);
    }

    public boolean getAlignLineComments() {
        return this.setting.getBooleanValue(ALIGN_LINE_COMMENTS, false);
    }

    public boolean getSpacesInParentheses() {
        return this.setting.getBooleanValue(SPACES_IN_PARENTHESES, false);
    }

}
