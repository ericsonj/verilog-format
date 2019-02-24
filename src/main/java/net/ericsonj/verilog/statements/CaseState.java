package net.ericsonj.verilog.statements;

import net.ericsonj.verilog.StatementState;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class CaseState extends StatementState {

    public enum STATE {
        INIT,
        CASE
    }

    private STATE state;

    public CaseState() {
        super("case", 0);
        this.state = STATE.INIT;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

}
