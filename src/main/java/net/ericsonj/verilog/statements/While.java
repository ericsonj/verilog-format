package net.ericsonj.verilog.statements;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 16, 2019 12:05:55 PM
 */
public class While extends AbstractStatement<WhileState> {

    private static final String KEYWORD_WHILE = "while";
    private static final String KEYWORD_BEGIN = "begin";
    private static final String KEYWORD_END = "end";
    public static final String LINE_COMMENT = "[ ]*[//|/*].*";

    @Override
    public boolean isInitStatement(FileFormat format, String line) {
        return matchesWhile(line) || matchesWhileBegin(line);
    }

    @Override
    public WhileState getInitStateElement(FileFormat format, String liine) {
        WhileState state = new WhileState();
        state.setBaseIndent(format.getCountIndent());
        state.setState(WhileState.STATE.INIT);
        return state;
    }

    @Override
    public Class<WhileState> getStateType() {
        return WhileState.class;
    }

    @Override
    public String stateMachine(FileFormat format, WhileState state, String line) {
        switch (state.getState()) {
            case INIT:
                if (matchesWhile(line)) {
                    format.addCountIndent();
                    state.setState(WhileState.STATE.WHILE);
                    state.setStateBlock(WhileState.STATE_BLOCK.MAYBE_BLOCK);
                    return indent(format, state.getBaseIndent(), line);
                }
                if (matchesWhileBegin(line)) {
                    format.addCountIndent();
                    state.setState(WhileState.STATE.WHILE);
                    state.setStateBlock(WhileState.STATE_BLOCK.IN_BLOCK);
                    return indent(format, state.getBaseIndent(), line);
                }
                break;
            case WHILE:
                switch (state.getStateBlock()) {
                    case INIT:
                        break;
                    case MAYBE_BLOCK:
                        if (matchesBegin(line)) {
                            state.setStateBlock(WhileState.STATE_BLOCK.IN_BLOCK);
                            return indent(format, state.getBaseIndent(), line);
                        } else if (matchesEmpty(line)) {
                            format.resCountIndent();
                            format.states.poll();
                            return indent(format, state.getBaseIndent(), line);
                        } else {
                            state.setState(WhileState.STATE.WHILE);
                            state.setStateBlock(WhileState.STATE_BLOCK.NO_BLOCK);
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

    private boolean matchesWhile(String line) {
        String base = "[ ]*" + KEYWORD_WHILE + ".*[)]";
        return StringHelper.stringMatches(line, base, base + LINE_COMMENT);
    }

    private boolean matchesWhileBegin(String line) {
        String base = "[ ]*" + KEYWORD_WHILE + "[ ]*[(].*[)][ ]*" + KEYWORD_BEGIN;
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
