package net.ericsonj.verilog.statements;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class Task extends AbstractStatement<TaskState> {

    public static final String KEYWORD_TASK = "task";
    public static final String KEYWORD_ENDTASK = "endtask";
    public static final String KEYWORD_BEGIN = "begin";
    public static final String KEYWORD_END = "end";
    public static final String LINE_COMMENT = "[ ]*[//|/*].*";

    @Override
    public boolean isInitStatement(FileFormat format, String line) {
        return matchesTask(line);
    }

    @Override
    public TaskState getInitStateElement(FileFormat format, String liine) {
        TaskState state = new TaskState();
        state.setBaseIndent(format.getCountIndent());
        state.setState(TaskState.STATE.INIT);
        return state;
    }

    @Override
    public Class<TaskState> getStateType() {
        return TaskState.class;
    }

    @Override
    public String stateMachine(FileFormat format, TaskState state, String line) {

        switch (state.getState()) {
            case INIT:
                if (matchesTask(line)) {
                    format.addCountIndent();
                    state.setState(TaskState.STATE.TASK);
                    return indent(format, state.getBaseIndent(), line);
                }
                break;
            case TASK:
                if (matchesBegin(line)) {
                    int cIndent = format.getCountIndent();
                    format.addCountIndent();
                    return indent(format, cIndent, line);
                } else if (matchesEnd(line)) {
                    format.resCountIndent();
                    return indent(format, line);
                } else if (matchesEndfunction(line)) {
                    format.states.poll();
                    format.setCountIndent(state.getBaseIndent());
                    return indent(format, state.getBaseIndent(), line);
                }
                break;
            default:
                throw new AssertionError(state.getState().name());
        }

        return null;

    }

    private boolean matchesTask(String line) {

        String baseTask = "[ ]*" + KEYWORD_TASK + "[ ]+?.*";

        return StringHelper.stringMatches(
                line,
                baseTask,
                baseTask + LINE_COMMENT);
    }

    private boolean matchesBegin(String line) {
        String basic = ".*[ ]*" + KEYWORD_BEGIN;
        String comment = LINE_COMMENT;
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

    private boolean matchesEndfunction(String line) {
        String basic = "[ ]*" + KEYWORD_ENDTASK;
        String comment = LINE_COMMENT;
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

    private boolean matchesEnd(String line) {
        String basic = ".*[ ]*" + KEYWORD_END;
        String comment = LINE_COMMENT;
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

}
