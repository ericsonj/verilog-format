package net.ericsonj.verilog.statements;

import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.LineIndentable;
import net.ericsonj.verilog.StatementState;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class If extends LineIndentable {

    @Override
    public String indentLine(FileFormat format, String line) {

        if (line.matches("[ ]*if[ ]*.*[)]") || line.matches("[ ]*if[ ]*.*[ ]*begin")) {
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
                if (line.matches("[ ]*if[ ]*.*[)]")) {
                    format.addCountIndent();
                    ifState.setState(IfState.IF_STATE.IF);
                    ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                    return indent(format, ifState.getBaseIndent(), line);
                }
                if (line.matches("[ ]*if[ ]*.*[ ]*begin")) {
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
                        if (line.matches("[ ]*begin")) {
                            ifState.setState(IfState.IF_STATE.IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else {
                            ifState.setState(IfState.IF_STATE.IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.NO_BLOCK);
                        }
                        break;
                    case IN_BLOCK:
                        if (line.matches("[ ]*end")) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.INIT);
                            return indent(format, ifState.getBaseIndent(), line);
                        }
                        break;
                    case NO_BLOCK:
                        if (line.matches("[ ]*else[ ]*if[ ]*.*[)]")) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*else[ ]*if[ ]*.*[ ]*begin")) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*else")) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*else[ ]*begin")) {
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
                        if (line.matches("[ ]*else[ ]*if[ ]*.*[)]")) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*else[ ]*if[ ]*.*[ ]*begin")) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*else")) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*else[ ]*begin")) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else {
                            format.resCountIndent();
                            format.states.poll();
                        }
                        break;
                    case MAYBE_BLOCK:
                        if (line.matches("[ ]*begin")) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.NO_BLOCK);
                        }
                        break;
                    case IN_BLOCK:
                        if (line.matches("[ ]*end")) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.INIT);
                            return indent(format, ifState.getBaseIndent(), line);
                        }
                        break;
                    case NO_BLOCK:
                        if (line.matches("[ ]*else")) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*else[ ]*begin")) {
                            ifState.setState(IfState.IF_STATE.ELSE);
                            ifState.setStateBlock(IfState.BLOCK_STATE.IN_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*else[ ]*if[ ]*.*[)]")) {
                            ifState.setState(IfState.IF_STATE.ELSE_IF);
                            ifState.setStateBlock(IfState.BLOCK_STATE.MAYBE_BLOCK);
                            return indent(format, ifState.getBaseIndent(), line);
                        } else if (line.matches("[ ]*else[ ]*if[ ]*.*[ ]*begin")) {
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
                        break;
                    case MAYBE_BLOCK:
                        if (line.matches("[ ]*begin")) {
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
                        if (line.matches("[ ]*end")) {
                            format.resCountIndent();
                            format.states.poll();
                            return indent(format, ifState.getBaseIndent(), line);
                        }
                        break;
                    case NO_BLOCK:
                        if (line.matches("[ ]*")) {
                            format.resCountIndent();
                            format.states.poll();
                        } else if (line.matches("[ ]*end") || line.matches("[ ]*endmodule")) {
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

}
