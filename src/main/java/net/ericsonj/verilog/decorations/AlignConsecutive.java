package net.ericsonj.verilog.decorations;

import java.util.LinkedList;
import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author ericson
 */
public class AlignConsecutive extends AbstractLineDecoration {

    private static final String COMMNET = "[ ]*[//|/*].*";

    public enum STATE {
        INIT,
        IS_CONSECUTIVE,
        CONSECUTIVE
    }

    private STATE state = STATE.INIT;
    private int alignIndex;
    private int startLineIndex;
    private int endLineIndex;
    private LinkedList<AssignmentLine> assignmentsBlocks = new LinkedList<>();

    private String beforeKeyRegex;
    private String key;
    private String afterKeyRegex;

    public AlignConsecutive(String beforeKeyRegex, String KeyRegex, String afterKeyRegex) {
        this.beforeKeyRegex = beforeKeyRegex;
        this.key = KeyRegex;
        this.afterKeyRegex = afterKeyRegex;
    }

    private String getLinePattern() {
        return beforeKeyRegex + key + afterKeyRegex;
    }

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {
        super.applyStyle(format, buffer); //To change body of generated methods, choose Tools | Templates.

        assignmentsBlocks.forEach((assignmentsBlock) -> {

            for (int i = assignmentsBlock.getStartLineIndex(); i <= assignmentsBlock.getEndLineIndex(); i++) {
                String line = buffer.get(i);
                int assignmentIdx = line.indexOf(getKey());
                if (assignmentIdx < assignmentsBlock.getAlignValue()) {
                    line = line.replaceAll(getKey(), StringHelper.getSpaces(assignmentsBlock.getAlignValue() - assignmentIdx) + getKey());
                    if (getKey().equals("=")) {
                        line = line.replaceAll("[ ]*([=!<>&|~^+\\-*/])[ ]*=[ ]*", " $1= ");
                    }
                }
                buffer.remove(i);
                buffer.add(i, line);
            }

        });
    }

    @Override
    public String decorateLine(FileFormat format, String line, int lineIndex) {
        switch (state) {
            case INIT:
                if (line.matches(getLinePattern()) || line.matches(getLinePattern() + COMMNET)) {
                    alignIndex = line.indexOf(getKey());
                    startLineIndex = lineIndex;
                    state = STATE.IS_CONSECUTIVE;
                }
                break;
            case IS_CONSECUTIVE:
                if (line.matches(getLinePattern()) || line.matches(getLinePattern() + COMMNET)) {
                    int aux = line.indexOf(getKey());
                    if (aux > alignIndex) {
                        alignIndex = aux;
                    }
                    endLineIndex = lineIndex;
                    state = STATE.CONSECUTIVE;
                } else {
                    state = STATE.INIT;
                }
                break;
            case CONSECUTIVE:
                if (line.matches(getLinePattern()) || line.matches(getLinePattern() + COMMNET)) {
                    int aux = line.indexOf(getKey());
                    if (aux > alignIndex) {
                        alignIndex = aux;
                    }
                    endLineIndex = lineIndex;
                    state = STATE.CONSECUTIVE;
                } else {
                    assignmentsBlocks.add(new AssignmentLine(startLineIndex, endLineIndex, alignIndex));
                    alignIndex = 0;
                    state = STATE.INIT;
                }
                break;
            default:
                throw new AssertionError(state.name());
        }
        return line;
    }

    @Override
    public boolean inBlockComment() {
        return false;
    }

    public String getBeforeKeyRegex() {
        return beforeKeyRegex;
    }

    public void setBeforeKeyRegex(String beforeKeyRegex) {
        this.beforeKeyRegex = beforeKeyRegex;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAfterKeyRegex() {
        return afterKeyRegex;
    }

    public void setAfterKeyRegex(String afterKeyRegex) {
        this.afterKeyRegex = afterKeyRegex;
    }

    public class AssignmentLine {

        private int startLineIndex;
        private int endLineIndex;
        private int alignValue;

        public AssignmentLine(int startLineIndex, int endLineIndex, int alignValue) {
            this.startLineIndex = startLineIndex;
            this.endLineIndex = endLineIndex;
            this.alignValue = alignValue;
        }

        public int getStartLineIndex() {
            return startLineIndex;
        }

        public void setStartLineIndex(int startLineIndex) {
            this.startLineIndex = startLineIndex;
        }

        public int getEndLineIndex() {
            return endLineIndex;
        }

        public void setEndLineIndex(int endLineIndex) {
            this.endLineIndex = endLineIndex;
        }

        public int getAlignValue() {
            return alignValue;
        }

        public void setAlignValue(int alignValue) {
            this.alignValue = alignValue;
        }

        @Override
        public String toString() {
            return "AssignmentLine{" + "startLineIndex=" + startLineIndex + ", endLineIndex=" + endLineIndex + ", alignValue=" + alignValue + '}';
        }

    }

}
