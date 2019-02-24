package net.ericsonj.verilog.statements;

import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.LineIndentable;
import net.ericsonj.verilog.StatementState;

/**
 *
 * @author ericson
 * @param <E>
 */
public abstract class AbstractStatement<E extends StatementState> extends LineIndentable {

    @Override
    public String indentLine(FileFormat format, String line) {

        if (isInitStatement(format, line)) {
            E state = getInitStateElement(format, line);
            format.states.push(state);
        }

        if (format.states.isEmpty()) {
            return null;
        }

        StatementState state = format.states.peek();
        boolean inState = getStateType().isAssignableFrom(state.getClass());

        if (!inState) {
            return null;
        }

        return stateMachine(format, (E) state, line);

    }

    public abstract boolean isInitStatement(FileFormat format, String line);

    public abstract E getInitStateElement(FileFormat format, String liine);

    public abstract Class<E> getStateType();

    public abstract String stateMachine(FileFormat format, E state, String line);

}
