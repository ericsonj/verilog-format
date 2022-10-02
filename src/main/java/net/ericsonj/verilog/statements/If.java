package net.ericsonj.verilog.statements;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.LineIndentable;
import net.ericsonj.verilog.StatementState;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class If extends LineIndentable {

    private static final String COMMNET = "[ ]*[//|/*].*";

    @Override
    public String indentLine(FileFormat format, String line) {

        if (matchesIf(line) || matchesIfBegin(line)) {
            IfState state = new IfState();
            state.setBaseIndent(format.getCountIndent());
            state.setState(IfState.IF_STATE.INIT);
            state.setStateBlock(IfState.BLOCK_STATE.INIT);
            format.states.push(state);
        }

        if (format.states.isEmpty()) {
            return null;
        }

        StatementState state = format.states.peek();
        boolean inState = state instanceof IfState;

        if (!inState) {
            return null;
        }

        IfState ifState = (IfState) state;

        switch (ifState.getState()) {
            case INIT:
                if (matchesIf(line)) {
                    format.addCountIndent();
                    ifState.setState(IfState.IF_STATE.IF);
                    ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                    return indent(format, ifState.getBaseIndent(), line);
                }
                if (matchesIfBegin(line)) {
                    format.addCountIndent();
                    ifState.setState(IfState.IF_STATE.IF);
                    ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                    return indent(format, ifState.getBaseIndent(), line);
                }
                break;
            case IF:
                switch (ifState.getStateBlock()) {
                    case INIT:
                        break;
                    case MAYBE_BLOCK:
                        if (line.matches(COMMNET)) {
                            ifState.setState(IfState.IF_STATE.IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        }
                        if (matchesBegin(line)) {
                            ifState.setState(IfState.IF_STATE.IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else {
                            ifState.setState(IfState.IF_STATE.IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.NO_BLOCK);
                        }
                        break;
                    case IN_BLOCK:
                        if (matchesEnd(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.INIT);
                            return indent(format, ifState.getBaseIndent(), line);
                        }
                        break;
                    case NO_BLOCK:
                        if (matchesElseIf(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesElseIfBegin(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesElse(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesElseBegin(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*")) {
                            format.resCountIndent();
                            format.states.poll();
                        }
                        break;
                    default:
                        throw new AssertionError(ifState.getStateBlock().name());
                }
                break;
            case ELSE_IF:
                switch (ifState.getStateBlock()) {
                    case INIT:
                        if (matchesElseIf(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesElseIfBegin(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesElse(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesElseBegin(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else {
                            format.resCountIndent();
                            format.states.poll();
                        }
                        break;
                    case MAYBE_BLOCK:
                        if (line.matches(COMMNET)) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesBegin(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.NO_BLOCK);
                        }
                        break;
                    case IN_BLOCK:
                        if (matchesEnd(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.INIT);
                            return indent(format, ifState.getBaseIndent(), line);
                        }
                        break;
                    case NO_BLOCK:
                        if (matchesElse(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesElseBegin(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesElseIf(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesElseIfBegin(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*")) {
                            format.resCountIndent();
                            format.states.poll();
                        }
                        break;
                    default:
                        throw new AssertionError(ifState.getStateBlock().name());
                }
                break;
            case ELSE:
                switch (ifState.getStateBlock()) {
                    case INIT:
                        if (matchesElse(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesElseBegin(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        }
                        break;
                    case MAYBE_BLOCK:
                        if (line.matches(COMMNET)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (matchesBegin(line)) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*")) {
                            format.resCountIndent();
                            format.states.poll();
                        } else {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.NO_BLOCK);
                        }
                        break;
                    case IN_BLOCK:
                        if (matchesEnd(line)) {
                            format.resCountIndent();
                            format.states.poll();
                            return indent(format, ifState.getBaseIndent(), line);
                        }
                        break;
                    case NO_BLOCK:
                        if (line.matches("[ ]*")) {
                            format.resCountIndent();
                            format.states.poll();
                        } else if (matchesEnd(line) || line.matches("[ ]*\\bendmodule\\b")) {
                            format.resCountIndent();
                            format.states.poll();
                        }
                        break;
                    default:
                        throw new AssertionError(ifState.getStateBlock().name());
                }
                break;
            default:
                throw new AssertionError(ifState.getState().name());
        }

        return null;

    }

    private boolean matchesIf(String line) {
        String ifBase = "[ ]*\\bif\\b[ ]*.*[)]";
        return StringHelper.stringMatches(line, ifBase, ifBase + COMMNET);
    }

    private boolean matchesIfBegin(String line) {
        String ifBase = "[ ]*\\bif\\b[ ]*.*[ ]*\\bbegin\\b";
        return StringHelper.stringMatches(line, ifBase, ifBase + COMMNET);
    }

    private boolean matchesBegin(String line) {
        String ifBase = "[ ]*\\bbegin\\b";
        return StringHelper.stringMatches(line, ifBase, ifBase + COMMNET);
    }

    private boolean matchesEnd(String line) {
        String ifBase = "[ ]*\\bend\\b";
        return StringHelper.stringMatches(line, ifBase, ifBase + COMMNET);
    }

    private boolean matchesElse(String line) {
        String ifBase = "[ ]*\\belse\\b";
        return StringHelper.stringMatches(line, ifBase, ifBase + COMMNET);
    }
    
    private boolean matchesElseBegin(String line) {
        String ifBase = "[ ]*\\belse\\b[ ]*\\bbegin\\b";
        return StringHelper.stringMatches(line, ifBase, ifBase + COMMNET);
    }
    
    private boolean matchesElseIf(String line) {
        String ifBase = "[ ]*\\belse\\b[ ]*\\bif\\b[ ]*.*[)]";
        return StringHelper.stringMatches(line, ifBase, ifBase + COMMNET);
    }
    
    private boolean matchesElseIfBegin(String line) {
        String ifBase = "[ ]*\\belse\\b[ ]*\\bif\\b[ ]*.*[ ]*\\bbegin\\b";
        return StringHelper.stringMatches(line, ifBase, ifBase + COMMNET);
    }

}
