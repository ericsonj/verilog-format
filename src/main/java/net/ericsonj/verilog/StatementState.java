package net.ericsonj.verilog;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class StatementState {

    private final String stateName;
    private int baseIndent;

    public StatementState(String stateName, int baseIndent) {
        this.stateName = stateName;
        this.baseIndent = baseIndent;
    }

    public String getStateName() {
        return stateName;
    }

    public int getBaseIndent() {
        return baseIndent;
    }

    public void setBaseIndent(int baseIndent) {
        this.baseIndent = baseIndent;
    }

}
