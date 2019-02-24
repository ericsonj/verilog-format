package net.ericsonj.verilog;

import java.util.LinkedList;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public interface StyleImp {

    public void applyStyle(FileFormat format, LinkedList<String> buffer);

}
