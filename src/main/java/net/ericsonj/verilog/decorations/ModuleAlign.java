package net.ericsonj.verilog.decorations;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.StyleImp;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 18, 2019 10:37:47 PM
 */
public class ModuleAlign implements StyleImp {

    public enum COMMENT_STATE {
        BLOCK_COMMENT,
        LINE_COMMNET
    }

    private LinkedHashMap<String, String> commnets = new LinkedHashMap<>();

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {

        String align = format.getSetting().getStringValue("ModuleAlign", "BAS_Align");

//        if (align.equals("BAS_Align")) {
//            process(buffer);
//        }
        int startModuleLine = getIdxLineMatches(buffer, "[ ]*module[ ]+[a-zA-Z0-9-_,;&$# ]*.*", 0);
        if (startModuleLine == -1) {
            return;
        }

        int endModuleLine = getIdxLineMatches(buffer, ".*[)][ ]*[;][ ]*[//|/*]*.*", startModuleLine);
        if (endModuleLine == -1) {
            return;
        }

        removeComments(buffer, startModuleLine, endModuleLine);

        String moduleDef = getModuleInLine(buffer, startModuleLine, endModuleLine);

        LinkedList<String> resp = BASAlign(moduleDef);

        int commentAlign = getMostLargeLineSize(resp);

        for (int i = 0; i < resp.size(); i++) {
            String line = resp.get(i);
            String[] words = line.split(" ");
            String lastWord = words[words.length - 1];
            if (commnets.containsKey(lastWord)) {
                LinkedList<String> lines = getCommentAlign(line, commentAlign, commnets.get(lastWord));
                resp.remove(i);
                resp.addAll(i, lines);
            } else {
                if (lastWord.matches("[^ ]+[)];")) {
                    String newWordKey = lastWord.replace(");", "");
                    if (commnets.containsKey(newWordKey)) {
                        LinkedList<String> lines = getCommentAlign(line, commentAlign, commnets.get(newWordKey));
                        resp.remove(i);
                        resp.addAll(i, lines);
                    }
                }
            }
        }

        replaceInBuffer(buffer, startModuleLine, endModuleLine, resp);

    }

    private int getIdxLineMatches(LinkedList<String> buffer, String regex, int offset) {
        for (int i = offset; i < buffer.size(); i++) {
            String line = buffer.get(i);
            if (line.matches(regex)) {
                return i;
            }
        }
        return -1;
    }

    private String getModuleInLine(LinkedList<String> buffer, int startModuleLine, int endModuleLine) {
        StringBuilder sb = new StringBuilder();

        for (int i = startModuleLine; i < endModuleLine + 1; i++) {
            sb.append(buffer.get(i).trim());
            sb.append(' ');
        }

        String moduleDef = sb.toString().trim();

        moduleDef = orderLine(moduleDef);

        return moduleDef;

    }

    private void replaceInBuffer(LinkedList<String> buffer, int startModuleLine, int endModuleLine, LinkedList<String> bufferSrc) {
        int linesRemove = endModuleLine - startModuleLine + 1;
        for (int i = 0; i < linesRemove; i++) {
            buffer.remove(startModuleLine);
        }
        buffer.addAll(startModuleLine, bufferSrc);
    }

    private String indent(int indent, String line) {
        StringBuilder sb = new StringBuilder(line);
        for (int i = 0; i < indent; i++) {
            sb.insert(0, ' ');
        }
        return sb.toString();
    }

    private LinkedList<String> BASAlign(String moduleInLine) {

        LinkedList<String> resp = new LinkedList<>();

        boolean moduleWithParam = moduleInLine.contains("#(");

        int indentSize = 0;
        int initParamBrackt = 0;
        int endParamBrackt = 0;

        if (moduleWithParam) {

            initParamBrackt = moduleInLine.indexOf("#(");
            endParamBrackt = moduleInLine.indexOf(")");

            resp.add(moduleInLine.substring(0, initParamBrackt + 2));

            String paramArgs = moduleInLine.substring(initParamBrackt + 2, endParamBrackt);

            StringTokenizer st = new StringTokenizer(paramArgs, ",");
            int count = st.countTokens();
            if (count == 1) {
                resp.set(0, resp.getFirst() + paramArgs + moduleInLine.charAt(endParamBrackt));
            } else {
                for (int i = 0; i < count - 1; i++) {
                    String arg = st.nextToken();
                    if (i == 0) {
                        resp.set(0, resp.getFirst() + arg + ",");
                    } else {
                        resp.add(indent(initParamBrackt + 1, arg + ","));
                    }
                }

                String arg = st.nextToken();
                resp.addLast(indent(initParamBrackt + 1, arg) + moduleInLine.charAt(endParamBrackt));
            }
            indentSize = initParamBrackt;

        }

        int initBracket = moduleInLine.indexOf("(", moduleWithParam ? endParamBrackt : 0);
        int endBracket = moduleInLine.indexOf(")", moduleWithParam ? endParamBrackt + 1 : 0);

        int endParamLine = 0;
        if (moduleWithParam) {
            resp.add(indent(indentSize, moduleInLine.substring(moduleWithParam ? endParamBrackt + 1 : 0, initBracket + 1)));
            endParamLine = resp.size() - 1;
            indentSize++;
        } else {
            resp.add(moduleInLine.substring(0, initBracket + 1));
            indentSize = initBracket;
        }

        String moduleArgs = moduleInLine.substring(initBracket + 1, endBracket);
        if (moduleArgs.isEmpty()) {
            resp.set(endParamLine, resp.get(endParamLine) + moduleArgs + moduleInLine.substring(endBracket));
            return resp;
        }

        StringTokenizer st = new StringTokenizer(moduleArgs, ",");
        int count = st.countTokens();
        if (count == 1) {
            resp.set(endParamLine, resp.get(endParamLine) + moduleArgs + moduleInLine.substring(endBracket));
            return resp;
        }
        for (int i = 0; i < count - 1; i++) {
            String arg = st.nextToken();
            if (i == 0) {
                resp.set(endParamLine, resp.get(endParamLine) + arg + ",");
            } else {
                resp.add(indent(indentSize, arg + ","));
            }
        }

        String arg = st.nextToken();
        resp.addLast(indent(indentSize, arg) + moduleInLine.substring(endBracket));

        return resp;

    }

    private void removeComments(LinkedList<String> buffer, int startModuleLine, int endModuleLine) {

        COMMENT_STATE commentState = COMMENT_STATE.LINE_COMMNET;
        String blockComment = "";
        String blockKey = "";

        for (int i = startModuleLine; i < endModuleLine + 1; i++) {
            String line = buffer.get(i);
            line = orderLine(line);

            switch (commentState) {
                case LINE_COMMNET:
                    if (line.matches(".*/\\*.*\\*/")) {
                        Pattern p = Pattern.compile(".*[ ](.*)[ ](/\\*.*\\*/)");
                        Matcher m = p.matcher(line);
                        if (m.find()) {
                            String key = m.group(1);
                            String comment = m.group(2);
                            commnets.put(key, comment);
                        }
                    } else if (line.matches(".*//.*")) {
                        Pattern p = Pattern.compile(".*[ ](.*)[ ](//.*)");
                        Matcher m = p.matcher(line);
                        if (m.find()) {
                            String key = m.group(1);
                            String comment = m.group(2);
                            commnets.put(key, comment);
                        }
                    } else if (line.matches(".*/\\*.*")) {
                        Pattern p = Pattern.compile(".*[ ](.*)[ ](/\\*.*)");
                        Matcher m = p.matcher(line);
                        if (m.find()) {
                            String key = m.group(1);
                            String comment = m.group(2);
                            blockComment += comment;
                            blockKey = key;
                            commnets.put(key, comment);
                            commentState = COMMENT_STATE.BLOCK_COMMENT;
                        }
                    }
                    break;
                case BLOCK_COMMENT:
                    if (line.matches(".*\\*/")) {
                        blockComment += "\\" + line;
                        commnets.put(blockKey, blockComment);
                        commentState = COMMENT_STATE.LINE_COMMNET;
                    } else {
                        blockComment += "\\" + line;
                        line = "";
                    }
                    break;
                default:
                    throw new AssertionError(commentState.name());
            }

            line = line.replaceAll("/\\*.*", "");
            line = line.replaceAll("//.*", "");
            line = line.replaceAll(".*\\*/", "");
            buffer.remove(i);
            buffer.add(i, line);
        }
    }

    private String orderLine(String line) {
        String orderLine = line.replaceAll("[#][ ]*[(]", "#(");
        orderLine = orderLine.replaceAll("[ ]+", " ");
        orderLine = orderLine.replaceAll("[(][ ]*", "(");
        orderLine = orderLine.replaceAll("[ ]*[)]", ")");
        orderLine = orderLine.replaceAll("[)][ ]*[;]", ");");
        orderLine = orderLine.replaceAll("[ ]*[,][ ]*", ", ");
        return orderLine;
    }

    private int getMostLargeLineSize(LinkedList<String> buffer) {
        int size = 0;
        for (String line : buffer) {
            if (line.length() > size) {
                size = line.length();
            }
        }
        return size;
    }

    private LinkedList<String> getCommentAlign(String line, int lineAlign, String comment) {
        LinkedList<String> lines = new LinkedList<>();
        int spaces = lineAlign - line.length() + 1;
        if (spaces == -1) {
            lines.add(line);
            return lines;
        }

        StringTokenizer st = new StringTokenizer(comment, "\\");
        int count = st.countTokens();
        for (int j = 0; j < count; j++) {
            StringBuilder sb = new StringBuilder();
            if (j == 0) {
                sb.append(line);
            } else {
                for (int i = 0; i < line.length(); i++) {
                    sb.append(" ");
                }
            }
            for (int i = 0; i < spaces; i++) {
                sb.append(" ");
            }
            sb.append(st.nextToken());
            lines.add(sb.toString());
        }

        return lines;

    }

}
