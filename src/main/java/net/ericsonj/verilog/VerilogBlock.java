package net.ericsonj.verilog;

import java.util.LinkedList;

/**
 *
 * @author ericson
 */
public class VerilogBlock {

    private String header;
    private boolean headerIndentBefore;
    private String end;
    private boolean endIndentBefore;
    private boolean withoutEnd = false;
//    private boolean headerDetect = false;

    public VerilogBlock(String header, boolean headerIndentBefore, String end, boolean endIndentBefore) {
        this.header = header;
        this.headerIndentBefore = headerIndentBefore;
        this.end = end;
        this.endIndentBefore = endIndentBefore;
        if (this.end == null) {
            withoutEnd = true;
        }
    }
    
    public VerilogBlock(String header, String end) {
        this(header, true, end, false);
    }

//    public void process(FileFormat format, LinkedList<String> buffer) {
//        for (int index = 0; index < buffer.size(); index++) {
//            String newLine = getIndentLine(format, buffer, index);
//            buffer.remove(index);
//            buffer.add(index, newLine);
//        }
//    }

    public String process(FileFormat format, String line) {
        
        String tmp = line;
        if (line.matches(header)) {
//            this.headerDetect = true;
            
            if(headerIndentBefore) tmp = indentLine(format, line);
            format.addCountIndent();
            if(!headerIndentBefore) tmp = indentLine(format, line);
 
        } else if (end != null && line.matches(end) && format.getCountIndent() > 0) {
            
            if(endIndentBefore) tmp = indentLine(format, line);
            format.resCountIndent();
            if(!endIndentBefore) tmp = indentLine(format, line);
        
        } else if (line.isEmpty()) {
            tmp = line;
        } else {
            tmp = indentLine(format, line);
            if (withoutEnd && format.getCountIndent() > 0) {
                format.resCountIndent();
            }
        }
        return tmp;
    }

    private String indentLine(FileFormat format, String line) {
        StringBuilder sb = new StringBuilder(line);
        for (int i = 0; i < (format.getIndentSize() * format.getCountIndent()); i++) {
            sb.insert(0, format.getIndentType());
        }
        return sb.toString();
    }
}
