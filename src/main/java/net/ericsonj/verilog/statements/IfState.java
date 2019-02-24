package net.ericsonj.verilog.statements;

import net.ericsonj.verilog.StatementState;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class IfState extends StatementState {

    public enum IF_STATE {
        INIT,
        IF,
        ELSE_IF,
        ELSE
    }

    public enum BLOCK_STATE {
        INIT,
        MAYBE_BLOCK,
        IN_BLOCK,
        NO_BLOCK
    }

    private IfState.IF_STATE state;
    private IfState.BLOCK_STATE stateBlock;

    public IfState() {
        super("if", 0);
    }

    public IfState.IF_STATE getState() {
        return state;
    }

    public void setState(IfState.IF_STATE state) {
        this.state = state;
    }

    public BLOCK_STATE getStateBlock() {
        return stateBlock;
    }

    public void setStateBlock(BLOCK_STATE stateBlock) {
        this.stateBlock = stateBlock;
    }

}
