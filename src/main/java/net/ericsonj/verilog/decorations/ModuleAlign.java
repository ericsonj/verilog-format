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

    private String keyWord;

    public ModuleAlign() {
        this("module");
    }

    public ModuleAlign(String keyWord) {
        this.keyWord = keyWord;
    }

    private LinkedHashMap<String, String> commnets = new LinkedHashMap<>();

    @Override
    public void applyStyle(FileFormat format, LinkedList<String> buffer) {

        String align = format.getSetting().getStringValue("ModuleAlign", "BAS_Align");

//        if (align.equals("BAS_Align")) {
//            process(buffer);
//        }
        int offset = 0;
        while (offset < buffer.size()) {
            int startModuleLine = getIdxLineMatches(buffer, "[ ]*" + keyWord + "[ ]+[a-zA-Z0-9-_,;&$# ]*.*", offset);
            if (startModuleLine == -1) {
                return;
            }

            int endModuleLine = getEndModuleLine(buffer, startModuleLine);
            if (endModuleLine == -1) {
                return;
            }

            commnets.clear();
            removeComments(buffer, startModuleLine, endModuleLine);

            String moduleDef = getModuleInLine(buffer, startModuleLine, endModuleLine);

            LinkedList<String> resp = BASAlign(format, moduleDef);

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
            offset = startModuleLine + resp.size();
        }

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

    private int getEndModuleLine(LinkedList<String> buffer, int startModuleLine) {
        if ("module".equals(keyWord)) {
            int depth = 0;
            for (int i = startModuleLine; i < buffer.size(); i++) {
                String line = buffer.get(i);
                depth += countChar(line, '(');
                depth -= countChar(line, ')');
                if (depth <= 0 && line.matches(".*;[ ]*[//|/*]*.*")) {
                    return i;
                }
            }
            return -1;
        }

        return getIdxLineMatches(buffer, ".*[)][ ]*[;][ ]*[//|/*]*.*", startModuleLine);
    }

    private String getModuleInLine(LinkedList<String> buffer, int startModuleLine, int endModuleLine) {
        StringBuilder sb = new StringBuilder();

        int startIndet = getIndent(buffer.get(startModuleLine));

        for (int i = startModuleLine; i < endModuleLine + 1; i++) {
            sb.append(buffer.get(i).trim());
            sb.append(' ');
        }

        String moduleDef = sb.toString().trim();

        moduleDef = orderLine(moduleDef);

        moduleDef = indent(startIndet, moduleDef);

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

    private int countChar(String value, char expected) {
        int count = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == expected) {
                count++;
            }
        }
        return count;
    }

    private int getIndent(String line) {
        int indent = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                indent++;
            } else {
                break;
            }
        }
        return indent;
    }

    private LinkedList<String> BASAlign(FileFormat format, String moduleInLine) {

        LinkedList<String> resp = new LinkedList<>();
        int baseIndent = getIndent(moduleInLine);
        String baseIndentStr = getSpaces(baseIndent);
        String content = moduleInLine.trim();
        String indentUnit = getSpaces(format.getIndentSize());

        boolean moduleWithParam = content.contains("#(");
        int paramClose = -1;

        if (moduleWithParam) {
            int paramOpen = content.indexOf("#(") + 1;
            paramClose = findClosingBracket(content, paramOpen);
            if (paramClose == -1) {
                resp.add(moduleInLine);
                return resp;
            }

            resp.add(baseIndentStr + content.substring(0, paramOpen + 1));

            LinkedList<String> paramArgs = splitTopLevelArgs(content.substring(paramOpen + 1, paramClose));
            if (paramArgs.size() == 1) {
                resp.set(0, resp.getFirst() + paramArgs.getFirst() + ")");
            } else {
                int paramIndent = resp.getFirst().length();
                for (int i = 0; i < paramArgs.size() - 1; i++) {
                    String arg = paramArgs.get(i);
                    if (i == 0) {
                        resp.set(0, resp.getFirst() + arg + ",");
                    } else {
                        resp.add(getSpaces(paramIndent) + arg + ",");
                    }
                }

                resp.addLast(getSpaces(paramIndent) + paramArgs.getLast() + ")");
            }
        }

        int initBracket = content.indexOf("(", moduleWithParam ? paramClose + 1 : 0);
        if (initBracket == -1) {
            resp.clear();
            resp.add(moduleInLine);
            return resp;
        }

        int endBracket = findClosingBracket(content, initBracket);
        if (endBracket == -1) {
            resp.clear();
            resp.add(moduleInLine);
            return resp;
        }

        if (moduleWithParam) {
            String instanceName = content.substring(paramClose + 1, initBracket).trim();
            resp.add(baseIndentStr + indentUnit + instanceName + "(");
        } else {
            resp.add(baseIndentStr + content.substring(0, initBracket + 1));
        }

        String moduleArgs = content.substring(initBracket + 1, endBracket);
        if (moduleArgs.isEmpty()) {
            int lastLine = resp.size() - 1;
            resp.set(lastLine, resp.get(lastLine) + content.substring(endBracket));
            return resp;
        }

        LinkedList<String> portArgs = splitTopLevelArgs(moduleArgs);
        if ("module".equals(keyWord)) {
            portArgs = alignModulePortDeclarations(portArgs);
        } else {
            portArgs = alignInstanceBrackets(portArgs);
        }
        int lastLine = resp.size() - 1;
        if (portArgs.size() == 1) {
            resp.set(lastLine, resp.get(lastLine) + moduleArgs + content.substring(endBracket));
            return resp;
        }

        String portIndent = baseIndentStr + (moduleWithParam ? indentUnit + indentUnit : indentUnit);
        for (int i = 0; i < portArgs.size() - 1; i++) {
            String arg = portArgs.get(i);
            resp.add(portIndent + arg + ",");
        }

        String closingIndent = moduleWithParam ? baseIndentStr + indentUnit : baseIndentStr;
        resp.addLast(portIndent + portArgs.getLast());
        resp.addLast(closingIndent + content.substring(endBracket));

        return resp;

    }

    private String getSpaces(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private int findClosingBracket(String value, int openIndex) {
        int depth = 0;
        for (int i = openIndex; i < value.length(); i++) {
            char current = value.charAt(i);
            if (current == '(') {
                depth++;
            } else if (current == ')') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private LinkedList<String> splitTopLevelArgs(String value) {
        LinkedList<String> args = new LinkedList<>();
        StringBuilder current = new StringBuilder();
        int parenthesisDepth = 0;
        int bracketDepth = 0;
        int braceDepth = 0;

        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            switch (ch) {
                case '(':
                    parenthesisDepth++;
                    break;
                case ')':
                    parenthesisDepth--;
                    break;
                case '[':
                    bracketDepth++;
                    break;
                case ']':
                    bracketDepth--;
                    break;
                case '{':
                    braceDepth++;
                    break;
                case '}':
                    braceDepth--;
                    break;
                case ',':
                    if (parenthesisDepth == 0 && bracketDepth == 0 && braceDepth == 0) {
                        args.add(current.toString().trim());
                        current.setLength(0);
                        continue;
                    }
                    break;
                default:
                    break;
            }
            current.append(ch);
        }

        args.add(current.toString().trim());
        return args;
    }

    private LinkedList<String> alignModulePortDeclarations(LinkedList<String> args) {
        LinkedList<String> alignedArgs = new LinkedList<>();
        String currentDeclarationPrefix = null;

        for (String arg : args) {
            String declarationPrefix = getModulePortDeclarationPrefix(arg);
            if (declarationPrefix != null) {
                currentDeclarationPrefix = declarationPrefix;
                alignedArgs.add(arg);
                continue;
            }

            if (currentDeclarationPrefix != null && isBareIdentifier(arg)) {
                alignedArgs.add(getSpaces(currentDeclarationPrefix.length() + 1) + arg.trim());
                continue;
            }

            currentDeclarationPrefix = null;
            alignedArgs.add(arg);
        }

        return alignedArgs;
    }

    private LinkedList<String> alignInstanceBrackets(LinkedList<String> args) {
        int maxPortNameLength = 0;
        LinkedList<String> alignedArgs = new LinkedList<>();

        for (String arg : args) {
            String portName = getNamedAssociationLeftSide(arg);
            if (portName != null && portName.length() > maxPortNameLength) {
                maxPortNameLength = portName.length();
            }
        }

        if (maxPortNameLength == 0) {
            alignedArgs.addAll(args);
            return alignedArgs;
        }

        for (String arg : args) {
            String portName = getNamedAssociationLeftSide(arg);
            if (portName == null) {
                alignedArgs.add(arg);
                continue;
            }

            int openBracketIndex = arg.indexOf('(');
            int closeBracketIndex = findClosingBracket(arg, openBracketIndex);
            if (openBracketIndex == -1 || closeBracketIndex == -1) {
                alignedArgs.add(arg);
                continue;
            }

            String value = arg.substring(openBracketIndex + 1, closeBracketIndex).trim();
            alignedArgs.add(portName + getSpaces(maxPortNameLength - portName.length() + 1) + "(" + value + ")");
        }

        return alignedArgs;
    }

    private String getNamedAssociationLeftSide(String arg) {
        int openBracketIndex = arg.indexOf('(');
        if (openBracketIndex <= 0 || !arg.trim().startsWith(".")) {
            return null;
        }

        String portName = arg.substring(0, openBracketIndex).trim();
        if (!portName.matches("\\.[A-Za-z_][A-Za-z0-9_$\\[\\]:]*")) {
            return null;
        }

        return portName;
    }

    private String getModulePortDeclarationPrefix(String arg) {
        String trimmed = arg.trim();
        if (!trimmed.matches("^(input|output|inout)\\b.*\\b[A-Za-z_][A-Za-z0-9_$]*$")) {
            return null;
        }

        int splitIndex = trimmed.lastIndexOf(' ');
        if (splitIndex == -1) {
            return null;
        }

        return trimmed.substring(0, splitIndex).trim();
    }

    private boolean isBareIdentifier(String arg) {
        return arg.trim().matches("^[A-Za-z_][A-Za-z0-9_$]*$");
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
        int startIndet = getIndent(line);
        String orderLine = line.replaceAll("[#][ ]*[(]", "#(");
        orderLine = orderLine.replaceAll("[ ]+", " ");
        orderLine = orderLine.replaceAll("[(][ ]*", "(");
        orderLine = orderLine.replaceAll("[ ]*[)]", ")");
        orderLine = orderLine.replaceAll("[)][ ]*[;]", ");");
        orderLine = orderLine.replaceAll("[ ]*[,][ ]*", ", ");
        orderLine = indent(startIndet, orderLine.trim());
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
