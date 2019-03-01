package net.ericsonj.verilog.decorations;

import java.util.LinkedList;
import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.StyleImp;

/**
 *
 * @author ejoseph
 */
public abstract class AbstractLineDecoration implements StyleImp {
    
    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {
        for (int i = 0; i < buffer.size(); i++) {
            String line = decorateLine(format, buffer.get(i));
            buffer.remove(i);
            buffer.add(i, line);
        }
    }
    
    public abstract String decorateLine(FileFormat format, String line);
    
}
