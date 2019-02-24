package net.ericsonj.verilog;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 */
public abstract class LineIndentable {

    public abstract String indentLine(FileFormat format, String line);

    public String indent(FileFormat format, String line) {
        StringBuilder sb = new StringBuilder(line);
        for (int i = 0; i < (format.getIndentSize() * format.getCountIndent()); i++) {
            sb.insert(0, format.getIndentType());
        }
        return sb.toString();
    }

    public String indent(FileFormat format, int baseIndent, String line) {
        StringBuilder sb = new StringBuilder(line);
        for (int i = 0; i < (format.getIndentSize() * baseIndent); i++) {
            sb.insert(0, format.getIndentType());
        }
        return sb.toString();
    }

}
