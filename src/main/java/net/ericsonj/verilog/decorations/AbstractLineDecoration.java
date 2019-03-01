package net.ericsonj.verilog.decorations;

import java.util.LinkedList;
import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.StyleImp;

/**
 *
 * @author ejoseph
 */
public abstract class AbstractLineDecoration implements StyleImp {

    public enum BLOCK_COMMNET_STATE {
        INIT,
        IN_BLOCK_COMMNET
    }

    private BLOCK_COMMNET_STATE state;

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {
        for (int i = 0; i < buffer.size(); i++) {
            String line = buffer.get(i);
            if (startBlockComment(line)) {
                state = BLOCK_COMMNET_STATE.IN_BLOCK_COMMNET;
            }
            if (endBlockComment(line)) {
                state = BLOCK_COMMNET_STATE.INIT;
            }
            if (state == BLOCK_COMMNET_STATE.INIT
                    || (state == BLOCK_COMMNET_STATE.IN_BLOCK_COMMNET && inBlockComment())) {
                line = decorateLine(format, line);
                buffer.remove(i);
                buffer.add(i, line);
            }
        }
    }

    public abstract String decorateLine(FileFormat format, String line);

    public abstract boolean inBlockComment();

    public boolean startBlockComment(String line) {
        return line.matches("/\\*.*");
    }

    public boolean endBlockComment(String line) {
        return line.matches(".*\\*/");
    }

}
