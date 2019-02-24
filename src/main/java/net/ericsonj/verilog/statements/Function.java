package net.ericsonj.verilog.statements;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class Function extends AbstractStatement<FunctionState> {

    public static final String KEYWORD_FUNCTION = "function";
    public static final String KEYWORD_ENDFUNCTION = "endfunction";
    public static final String KEYWORD_BEGIN = "begin";
    public static final String KEYWORD_END = "end";
    public static final String LINE_COMMENT = "[ ]*[//|/*].*";

    @Override
    public boolean isInitStatement(FileFormat format, String line) {
        return matchesFunction(line);
    }

    @Override
    public FunctionState getInitStateElement(FileFormat format, String liine) {
        FunctionState state = new FunctionState();
        state.setBaseIndent(format.getCountIndent());
        state.setState(FunctionState.STATE.INIT);
        return state;
    }

    @Override
    public Class<FunctionState> getStateType() {
        return FunctionState.class;
    }

    @Override
    public String stateMachine(FileFormat format, FunctionState state, String line) {

        switch (state.getState()) {
            case INIT:
                if (matchesFunction(line)) {
                    format.addCountIndent();
                    state.setState(FunctionState.STATE.FUNCTION);
                    return indent(format, state.getBaseIndent(), line);
                }
                break;
            case FUNCTION:
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

    private boolean matchesFunction(String line) {

        String baseFunction = "[ ]*" + KEYWORD_FUNCTION + "[\\[( ]?[ ]+?.*";

        return StringHelper.stringMatches(
                line,
                baseFunction,
                baseFunction + LINE_COMMENT);
    }

    private boolean matchesBegin(String line) {
        String basic = ".*[ ]*" + KEYWORD_BEGIN;
        String comment = LINE_COMMENT;
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

    private boolean matchesEndfunction(String line) {
        String basic = "[ ]*" + KEYWORD_ENDFUNCTION;
        String comment = LINE_COMMENT;
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

    private boolean matchesEnd(String line) {
        String basic = ".*[ ]*" + KEYWORD_END;
        String comment = LINE_COMMENT;
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

}
