package net.ericsonj.verilog.statements;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 16, 2019 12:05:55 PM
 */
public class Initial extends AbstractStatement<InitialState> {

    private static final String KEYWORD_INITIAL = "initial";
    private static final String KEYWORD_BEGIN = "begin";
    private static final String KEYWORD_END = "end";
    public static final String LINE_COMMENT = "[ ]*[//|/*].*";

    @Override
    public boolean isInitStatement(FileFormat format, String line) {
        return matchesInitial(line) || matchesInitialBegin(line);
    }

    @Override
    public InitialState getInitStateElement(FileFormat format, String liine) {
        InitialState state = new InitialState();
        state.setBaseIndent(format.getCountIndent());
        state.setState(InitialState.STATE.INIT);
        return state;
    }

    @Override
    public Class<InitialState> getStateType() {
        return InitialState.class;
    }

    @Override
    public String stateMachine(FileFormat format, InitialState state, String line) {
        switch (state.getState()) {
            case INIT:
                if (matchesInitial(line)) {
                    format.addCountIndent();
                    state.setState(InitialState.STATE.IN_INITIAL);
                    state.setStateBlock(InitialState.STATE_BLOCK.MAYBE_BLOCK);
                    return indent(format, state.getBaseIndent(), line);
                }
                if (matchesInitialBegin(line)) {
                    format.addCountIndent();
                    state.setState(InitialState.STATE.IN_INITIAL);
                    state.setStateBlock(InitialState.STATE_BLOCK.IN_BLOCK);
                    return indent(format, state.getBaseIndent(), line);
                }
                break;
            case IN_INITIAL:
                switch (state.getStateBlock()) {
                    case INIT:
                        break;
                    case MAYBE_BLOCK:
                        if (matchesBegin(line)) {
                            state.setStateBlock(InitialState.STATE_BLOCK.IN_BLOCK);
                            return indent(format, state.getBaseIndent(), line);
                        } else if (matchesEmpty(line)) {
                            format.resCountIndent();
                            format.states.poll();
                            return indent(format, state.getBaseIndent(), line);
                        } else {
                            state.setState(InitialState.STATE.IN_INITIAL);
                            state.setStateBlock(InitialState.STATE_BLOCK.NO_BLOCK);
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

    private boolean matchesInitial(String line) {
        String base = "[ ]*" + KEYWORD_INITIAL;
        return StringHelper.stringMatches(line, base, base + LINE_COMMENT);
    }

    private boolean matchesInitialBegin(String line) {
        String base = "[ ]*" + KEYWORD_INITIAL + "[ ]*" + KEYWORD_BEGIN;
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
