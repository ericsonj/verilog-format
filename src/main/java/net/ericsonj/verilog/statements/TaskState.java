package net.ericsonj.verilog.statements;

import net.ericsonj.verilog.StatementState;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class TaskState extends StatementState {

    public enum STATE {
        INIT,
        TASK
    }

    private STATE state;

    public TaskState() {
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
