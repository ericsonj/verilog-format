package net.ericsonj.verilog.statements;

import net.ericsonj.verilog.StatementState;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 28, 2019 12:43:30 AM
 */
public class BlockCommentState extends StatementState {

    public enum STATE {
        INIT,
        COMMENT
    }

    private BlockCommentState.STATE state;

    public BlockCommentState() {
        super("blockcomment", 0);
        this.state = STATE.INIT;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

}
