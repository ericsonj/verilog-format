package net.ericsonj.verilog.statements;

import net.ericsonj.verilog.StatementState;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 16, 2019 12:06:32 PM
 */
public class ForState extends StatementState {

    public enum STATE {
        INIT,
        FOR
    }

    public enum STATE_BLOCK {
        INIT,
        MAYBE_BLOCK,
        IN_BLOCK,
        NO_BLOCK
    }

    private STATE_BLOCK stateBlock;
    private STATE state;

    public ForState() {
        super("for", 0);
        this.stateBlock = STATE_BLOCK.INIT;
    }

    public STATE_BLOCK getStateBlock() {
        return stateBlock;
    }

    public void setStateBlock(STATE_BLOCK stateBlock) {
        this.stateBlock = stateBlock;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

}
