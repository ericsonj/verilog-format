package net.ericsonj.verilog.statements;

import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class Always extends AbstractStatement<AlwaysState> {

    @Override
    public boolean isInitStatement(FileFormat format, String line) {
        return matchesAlways(line) || matchesAlwaysBegin(line);
    }

    @Override
    public AlwaysState getInitStateElement(FileFormat format, String liine) {
        AlwaysState state = new AlwaysState();
        state.setBaseIndent(format.getCountIndent());
        state.setState(AlwaysState.STATE.INIT);
        return state;
    }

    @Override
    public Class<AlwaysState> getStateType() {
        return AlwaysState.class;
    }

    @Override
    public String stateMachine(FileFormat format, AlwaysState state, String line) {
        switch (state.getState()) {
            case INIT:
                if (matchesAlways(line)) {
                    format.addCountIndent();
                    state.setState(AlwaysState.STATE.MAYBE_BLOCK);
                    return indent(format, state.getBaseIndent(), line);
                } else if (matchesAlwaysBegin(line)) {
                    format.addCountIndent();
                    state.setState(AlwaysState.STATE.WAIT_ENDBLOCK);
                    return indent(format, state.getBaseIndent(), line);
                }
                break;
            case MAYBE_BLOCK:
                if (matchesBegin(line)) {
                    state.setState(AlwaysState.STATE.WAIT_ENDBLOCK);
                    return indent(format, state.getBaseIndent(), line);
                } else if (matchesEmpty(line)) {
                    format.resCountIndent();
                    format.states.poll();
                    return indent(format, state.getBaseIndent(), line);
                } else {
                    state.setState(AlwaysState.STATE.WAIT_END);
                }
                break;
            case WAIT_ENDBLOCK:
                if (matchesEnd(line)) {
                    format.resCountIndent();
                    format.states.poll();
                    return indent(format, state.getBaseIndent(), line);
                }
                break;
            case WAIT_END:
                if (matchesEmpty(line)) {
                    format.resCountIndent();
                    format.states.poll();
                } else if (line.matches("[ ]*endmodule") || line.matches("[ ]*end")) {
                    format.resCountIndent();
                    format.states.poll();
                }
                break;
            default:
                break;
        }

        return null;
    }

    private boolean matchesAlways(String line) {
        String basic = "[ ]*always[ ]*.*[)]";
        String comment = "[ ]*[//|/*].*";
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

    private boolean matchesAlwaysBegin(String line) {
        String basic = "[ ]*always[ ]*.*[ ]*begin";
        String comment = "[ ]*[//|/*].*";
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

    private boolean matchesBegin(String line) {
        String basic = "[ ]*begin";
        String comment = "[ ]*[//|/*].*";
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

    private boolean matchesEnd(String line) {
        String basic = "[ ]*end";
        String comment = "[ ]*[//|/*].*";
        return StringHelper.stringMatches(line, basic, basic + comment);
    }

    private boolean matchesEmpty(String line) {
        String basic = "[ ]*";
        return StringHelper.stringMatches(line, basic);
    }

}
