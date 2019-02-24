package net.ericsonj.verilog.statements;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class Case extends AbstractStatement<CaseState> {

    public static final String KEYWORD_CASE = "case";
    public static final String KEYWORD_CASEZ = "casez";
    public static final String KEYWORD_CASEX = "casex";
    public static final String KEYWORD_ENDCASE = "endcase";
    public static final String KEYWORD_BEGIN = "begin";
    public static final String KEYWORD_END = "end";
    public static final String LINE_COMMENT = "[ ]*[//|/*].*";

    @Override
    public boolean isInitStatement(FileFormat format, String line) {
        return matchesCase(line);
    }

    @Override
    public CaseState getInitStateElement(FileFormat format, String liine) {
        CaseState state = new CaseState();
        state.setBaseIndent(format.getCountIndent());
        state.setState(CaseState.STATE.INIT);
        return state;
    }

    @Override
    public Class<CaseState> getStateType() {
        return CaseState.class;
    }

    @Override
    public String stateMachine(FileFormat format, CaseState state, String line) {

        switch (state.getState()) {
            case INIT:
                if (matchesCase(line)) {
                    format.addCountIndent();
                    state.setState(CaseState.STATE.CASE);
                    return indent(format, state.getBaseIndent(), line);
                }
                break;
            case CASE:
                if (matchesBegin(line)) {
                    int cIndent = format.getCountIndent();
                    format.addCountIndent();
                    return indent(format, cIndent, line);
                } else if (matchesEnd(line)) {
                    format.resCountIndent();
                    return indent(format, line);
                } else if (matchesEndcase(line)) {
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

    private boolean matchesCase(String line) {

        String baseCase = "[ ]*" + KEYWORD_CASE + "[ ]*.*[)]";
        String baseCasex = "[ ]*" + KEYWORD_CASEX + "[ ]*.*[)]";
        String baseCasez = "[ ]*" + KEYWORD_CASEZ + "[ ]*.*[)]";

        return StringHelper.stringMatches(
                line,
                baseCase,
                baseCasex,
                baseCasez,
                baseCase + LINE_COMMENT,
                baseCasex + LINE_COMMENT,
                baseCasez + LINE_COMMENT);
    }

    private boolean matchesBegin(String line) {
        String basic = ".*[ ]*" + KEYWORD_BEGIN;
        String comment = LINE_COMMENT;
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

    private boolean matchesEndcase(String line) {
        String basic = "[ ]*" + KEYWORD_ENDCASE;
        String comment = LINE_COMMENT;
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

    private boolean matchesEnd(String line) {
        String basic = ".*[ ]*" + KEYWORD_END;
        String comment = LINE_COMMENT;
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

}
