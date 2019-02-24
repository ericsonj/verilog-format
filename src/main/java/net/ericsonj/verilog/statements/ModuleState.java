package net.ericsonj.verilog.statements;

import net.ericsonj.verilog.StatementState;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class ModuleState extends StatementState {

    public enum MODULE_STATE {
        INIT,
        WAIT_ENDMODULE
    }

    private MODULE_STATE state;

    public ModuleState() {
        super("module", 0);
        this.state = MODULE_STATE.INIT;
    }

    public MODULE_STATE getState() {
        return state;
    }

    public void setState(MODULE_STATE state) {
        this.state = state;
    }

}
