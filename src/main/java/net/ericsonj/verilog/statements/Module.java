package net.ericsonj.verilog.statements;

import java.util.LinkedList;
import net.ericsonj.util.StringHelper;
import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.LineIndentable;
import net.ericsonj.verilog.StatementState;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class Module extends LineIndentable {

    @Override
    public String indentLine(FileFormat format, String line) {

        if (matchesModule(line)) {
            ModuleState state = new ModuleState();
            state.setBaseIndent(format.getCountIndent());
            state.setState(ModuleState.MODULE_STATE.INIT);
            format.states.push(state);
        }

        if (format.states.isEmpty()) {
            return null;
        }

        StatementState state = format.states.peek();
        boolean inState = state instanceof ModuleState;

        if (!inState) {
            return null;
        }

        ModuleState moduleState = (ModuleState) state;

        switch (moduleState.getState()) {
            case INIT:
                format.addCountIndent();
                moduleState.setState(ModuleState.MODULE_STATE.WAIT_ENDMODULE);
                return indent(format, moduleState.getBaseIndent(), line);
            case WAIT_ENDMODULE:
                if (matchesEndmodule(line)) {
                    format.states.poll();
                    return indent(format, moduleState.getBaseIndent(), line);
                }
                break;
            default:
                return null;
        }

        return null;
    }

    private boolean matchesModule(String line) {

        LinkedList<String> opts = new LinkedList<>();

        String comment = "[ ]*[//|/*].*";
        String basicModule = "[ ]*module[ ]*[a-zA-Z0-9-_,;&$#()= ]*";

        opts.add(basicModule);
        opts.add(basicModule + "[(][a-zA-Z0-9-_,&$# ]*");
        opts.add(basicModule + "[(].*[)][;]?");
        opts.add(basicModule + comment);
        opts.add(basicModule + "[(][a-zA-Z0-9-_,&$# ]*" + comment);
        opts.add(basicModule + "[(].*[)]" + comment);

        return StringHelper.stringMatches(line, opts);

    }

    private boolean matchesEndmodule(String line) {
        LinkedList<String> opts = new LinkedList<>();

        String comment = "[ ]*[//|/*].*";
        opts.add("[ ]*endmodule");
        opts.add("[ ]*endmodule" + comment);

        return StringHelper.stringMatches(line, opts);

    }

}
