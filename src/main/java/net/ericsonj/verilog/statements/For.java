package net.ericsonj.verilog.statements;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 16, 2019 12:05:55 PM
 */
public class For extends AbstractStatement<ForState> {

    private static final String KEYWORD_FOR = "for";
    private static final String KEYWORD_BEGIN = "begin";
    private static final String KEYWORD_END = "end";
    public static final String LINE_COMMENT = "[ ]*[//|/*].*";

    @Override
    public boolean isInitStatement(FileFormat format, String line) {
        return matchesFor(line) || matchesForBegin(line);
    }

    @Override
    public ForState getInitStateElement(FileFormat format, String liine) {
        ForState state = new ForState();
        state.setBaseIndent(format.getCountIndent());
        state.setState(ForState.STATE.INIT);
        return state;
    }

    @Override
    public Class<ForState> getStateType() {
        return ForState.class;
    }

    @Override
    public String stateMachine(FileFormat format, ForState state, String line) {
        switch (state.getState()) {
            case INIT:
                if (matchesFor(line)) {
                    format.addCountIndent();
                    state.setState(ForState.STATE.FOR);
                    state.setStateBlock(ForState.STATE_BLOCK.MAYBE_BLOCK);
                    return indent(format, state.getBaseIndent(), line);
                }
                if (matchesForBegin(line)) {
                    format.addCountIndent();
                    state.setState(ForState.STATE.FOR);
                    state.setStateBlock(ForState.STATE_BLOCK.IN_BLOCK);
                    return indent(format, state.getBaseIndent(), line);
                }
                break;
            case FOR:
                switch (state.getStateBlock()) {
                    case INIT:
                        break;
                    case MAYBE_BLOCK:
                        if (matchesBegin(line)) {
                            state.setStateBlock(ForState.STATE_BLOCK.IN_BLOCK);
                            return indent(format, state.getBaseIndent(), line);
                        } else if (matchesEmpty(line)) {
                            format.resCountIndent();
                            format.states.poll();
                            return indent(format, state.getBaseIndent(), line);
                        } else {
                            state.setState(ForState.STATE.FOR);
                            state.setStateBlock(ForState.STATE_BLOCK.NO_BLOCK);
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

    private boolean matchesFor(String line) {
        String base = "[ ]*" + KEYWORD_FOR + ".*[)]";
        return StringHelper.stringMatches(line, base, base + LINE_COMMENT);
    }

    private boolean matchesForBegin(String line) {
        String base = "[ ]*" + KEYWORD_FOR + "[ ]*[(].*[)][ ]*" + KEYWORD_BEGIN;
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
