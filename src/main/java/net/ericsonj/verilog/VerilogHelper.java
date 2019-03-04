package net.ericsonj.verilog;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@comtor.net>
 */
public class VerilogHelper {

    public Set<String> keyWords = new HashSet<>();
    private static final VerilogHelper instance;

    static {
        instance = new VerilogHelper();
    }

    private VerilogHelper() {
        keyWords.add("always");
        keyWords.add("and");
        keyWords.add("assign");
        keyWords.add("automatic");
        keyWords.add("begin");
        keyWords.add("buf");
        keyWords.add("bufif0");
        keyWords.add("bufif1");
        keyWords.add("case");
        keyWords.add("casex");
        keyWords.add("casez");
        keyWords.add("cell");
        keyWords.add("cmos");
        keyWords.add("config");
        keyWords.add("deassign");
        keyWords.add("default");
        keyWords.add("defparam");
        keyWords.add("design");
        keyWords.add("disable");
        keyWords.add("edge");
        keyWords.add("else");
        keyWords.add("end");
        keyWords.add("endcase");
        keyWords.add("endconfig");
        keyWords.add("endfunction");
        keyWords.add("endgenerate");
        keyWords.add("endmodule");
        keyWords.add("endprimitive");
        keyWords.add("endspecify");
        keyWords.add("endtable");
        keyWords.add("endtask");
        keyWords.add("event");
        keyWords.add("for");
        keyWords.add("force");
        keyWords.add("forever");
        keyWords.add("fork");
        keyWords.add("function");
        keyWords.add("generate");
        keyWords.add("genvar");
        keyWords.add("highz0");
        keyWords.add("highz1");
        keyWords.add("if");
        keyWords.add("ifnone");
        keyWords.add("incdir");
        keyWords.add("include");
        keyWords.add("initial");
        keyWords.add("inout");
        keyWords.add("input");
        keyWords.add("instance");
        keyWords.add("integer");
        keyWords.add("join");
        keyWords.add("large");
        keyWords.add("liblist");
        keyWords.add("library");
        keyWords.add("localparam");
        keyWords.add("macromodule");
        keyWords.add("medium");
        keyWords.add("module");
        keyWords.add("nand");
        keyWords.add("negedge");
        keyWords.add("nmos");
        keyWords.add("nor");
        keyWords.add("noshowcancelledno");
        keyWords.add("not");
        keyWords.add("notif0");
        keyWords.add("notif1");
        keyWords.add("or");
        keyWords.add("output");
        keyWords.add("parameter");
        keyWords.add("pmos");
        keyWords.add("posedge");
        keyWords.add("primitive");
        keyWords.add("pull0");
        keyWords.add("pull1");
        keyWords.add("pulldown");
        keyWords.add("pullup");
        keyWords.add("pulsestyle_oneventglitch");
        keyWords.add("pulsestyle_ondetectglitch");
        keyWords.add("remos");
        keyWords.add("real");
        keyWords.add("realtime");
        keyWords.add("reg");
        keyWords.add("release");
        keyWords.add("repeat");
        keyWords.add("rnmos");
        keyWords.add("rpmos");
        keyWords.add("rtran");
        keyWords.add("rtranif0");
        keyWords.add("rtranif1");
        keyWords.add("scalared");
        keyWords.add("showcancelled");
        keyWords.add("signed");
        keyWords.add("small");
        keyWords.add("specify");
        keyWords.add("specparam");
        keyWords.add("strong0");
        keyWords.add("strong1");
        keyWords.add("supply0");
        keyWords.add("supply1");
        keyWords.add("table");
        keyWords.add("task");
        keyWords.add("time");
        keyWords.add("tran");
        keyWords.add("tranif0");
        keyWords.add("tranif1");
        keyWords.add("tri");
        keyWords.add("tri0");
        keyWords.add("tri1");
        keyWords.add("triand");
        keyWords.add("trior");
        keyWords.add("trireg");
        keyWords.add("unsigned");
        keyWords.add("use");
        keyWords.add("vectored");
        keyWords.add("wait");
        keyWords.add("wand");
        keyWords.add("weak0");
        keyWords.add("weak1");
        keyWords.add("while");
        keyWords.add("wire");
        keyWords.add("wor");
        keyWords.add("xnor");
        keyWords.add("xor");
    }

    public static VerilogHelper getInstance() {
        return instance;
    }

    public static boolean isKeyWord(String word) {
        return getInstance().keyWords.contains(word);
    }

}
