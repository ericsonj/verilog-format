package net.ericsonj.verilog.statements;

import net.ericsonj.verilog.FileFormat;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 28, 2019 12:42:31 AM
 */
public class BlockComment extends AbstractStatement<BlockCommentState> {

    @Override
    public boolean isInitStatement(FileFormat format, String line) {
        return line.matches("/\\*.*");
    }

    @Override
    public BlockCommentState getInitStateElement(FileFormat format, String liine) {
        BlockCommentState state = new BlockCommentState();
        state.setBaseIndent(format.getCountIndent());
        state.setState(BlockCommentState.STATE.INIT);
        return state;
    }

    @Override
    public Class<BlockCommentState> getStateType() {
        return BlockCommentState.class;
    }

    @Override
    public String stateMachine(FileFormat format, BlockCommentState state, String line) {
        switch (state.getState()) {
            case INIT:
                if (line.matches("/\\*.*") && line.matches(".*\\*/")) {
                    format.states.poll();
                } else if (line.matches("/\\*.*")) {
                    state.setState(BlockCommentState.STATE.COMMENT);
                    return indent(format, line);
                }
                break;
            case COMMENT:
                if (line.matches(".*\\*/")) {
                    format.states.poll();
                }
                return indent(format, " " + line);
            default:
                throw new AssertionError(state.getState().name());
        }
        return null;
    }

}
