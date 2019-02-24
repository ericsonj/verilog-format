package net.ericsonj.verilog.statements;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 16, 2019 12:05:55 PM
 */
public class Repeat extends AbstractStatement<RepeatState> {

    private static final String KEYWORD_REPEAT = "repeat";
    private static final String KEYWORD_BEGIN = "begin";
    private static final String KEYWORD_END = "end";
    public static final String LINE_COMMENT = "[ ]*[//|/*].*";

    @Override
    public boolean isInitStatement(FileFormat format, String line) {
        return matchesRepeat(line) || matchesRepeatBegin(line);
    }

    @Override
    public RepeatState getInitStateElement(FileFormat format, String liine) {
        RepeatState state = new RepeatState();
        state.setBaseIndent(format.getCountIndent());
        state.setState(RepeatState.STATE.INIT);
        return state;
    }

    @Override
    public Class<RepeatState> getStateType() {
        return RepeatState.class;
    }

    @Override
    public String stateMachine(FileFormat format, RepeatState state, String line) {
        switch (state.getState()) {
            case INIT:
                if (matchesRepeat(line)) {
                    format.addCountIndent();
                    state.setState(RepeatState.STATE.REPEAT);
                    state.setStateBlock(RepeatState.STATE_BLOCK.MAYBE_BLOCK);
                    return indent(format, state.getBaseIndent(), line);
                }
                if (matchesRepeatBegin(line)) {
                    format.addCountIndent();
                    state.setState(RepeatState.STATE.REPEAT);
                    state.setStateBlock(RepeatState.STATE_BLOCK.IN_BLOCK);
                    return indent(format, state.getBaseIndent(), line);
                }
                break;
            case REPEAT:
                switch (state.getStateBlock()) {
                    case INIT:
                        break;
                    case MAYBE_BLOCK:
                        if (matchesBegin(line)) {
                            state.setStateBlock(RepeatState.STATE_BLOCK.IN_BLOCK);
                            return indent(format, state.getBaseIndent(), line);
                        } else if (matchesEmpty(line)) {
                            format.resCountIndent();
                            format.states.poll();
                            return indent(format, state.getBaseIndent(), line);
                        } else {
                            state.setState(RepeatState.STATE.REPEAT);
                            state.setStateBlock(RepeatState.STATE_BLOCK.NO_BLOCK);
                        }
                        break;
                    case IN_BLOCK:
                        if (matchesEnd(line)) {
                            format.resCountIndent();
                            format.states.poll();
                            return indent(format, state.getBaseIndent(), line);
                        }
                        break;
                    case NO_BLOCK:
                        if (matchesEmpty(line) || line.matches("[ ]*endmodule") || line.matches("[ ]*end")) {
                            format.resCountIndent();
                            format.states.poll();
                        }
                        break;
                    default:
                        throw new AssertionError(state.getStateBlock().name());
                }
                break;



            default:
                throw new AssertionError(state.getState().name());
        }

        return null;
    }

    private boolean matchesRepeat(String line) {
        String base = "[ ]*" + KEYWORD_REPEAT+".*[)]";
        return StringHelper.stringMatches(line, base, base + LINE_COMMENT);
    }

    private boolean matchesRepeatBegin(String line) {
        String base = "[ ]*" + KEYWORD_REPEAT + "[ ]*[(].*[)][ ]*" + KEYWORD_BEGIN;
        return StringHelper.stringMatches(line, base, base + LINE_COMMENT);
    }

    private boolean matchesBegin(String line) {
        String base = "[ ]*" + KEYWORD_BEGIN;
        return StringHelper.stringMatches(line, base, base + LINE_COMMENT);
    }

    private boolean matchesEmpty(String line) {
        String base = "[ ]*";
        return StringHelper.stringMatches(line, base);
    }

    private boolean matchesEnd(String line) {
        String base = "[ ]*" + KEYWORD_END;
        return StringHelper.stringMatches(line, base, base + LINE_COMMENT);
    }

}
