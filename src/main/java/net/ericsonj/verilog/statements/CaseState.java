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
    private boolean inCaseItem;

    public CaseState() {
        super("case", 0);
        this.state = STATE.INIT;
        this.inCaseItem = false;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public boolean isInCaseItem() {
        return inCaseItem;
    }

    public void setInCaseItem(boolean inCaseItem) {
        this.inCaseItem = inCaseItem;
    }

}
