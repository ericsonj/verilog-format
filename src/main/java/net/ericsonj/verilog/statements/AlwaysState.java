package net.ericsonj.verilog.statements;

import net.ericsonj.verilog.StatementState;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class AlwaysState extends StatementState {

    public enum STATE {
        INIT,
        MAYBE_BLOCK,
        WAIT_ENDBLOCK,
        WAIT_END,
    }

    private AlwaysState.STATE state;

    public AlwaysState() {
        super("always", 0);
    }

    public AlwaysState.STATE getState() {
        return state;
    }

    public void setState(AlwaysState.STATE state) {
        this.state = state;
    }

}
