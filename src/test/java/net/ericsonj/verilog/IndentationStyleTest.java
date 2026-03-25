package net.ericsonj.verilog;

import java.util.Arrays;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Test;

public class IndentationStyleTest {

    @Test
    public void alignsAlwaysEndWithAlwaysAndEndmoduleWithModule() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "module demo;",
                "always @(posedge clk) begin",
                "data <= next_data;",
                "end",
                "endmodule"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new IndentationStyle().applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    always @(posedge clk) begin",
                "        data <= next_data;",
                "    end",
                "endmodule"
        ), buffer);
    }

    @Test
    public void exitsSingleLineAlwaysBeforeTheNextModuleLevelStatement() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "module demo;",
                "always @(posedge clk)",
                "a <= b;",
                "always @(posedge clk)",
                "c <= d;",
                "endmodule // demo"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new IndentationStyle().applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    always @(posedge clk)",
                "        a <= b;",
                "    always @(posedge clk)",
                "        c <= d;",
                "endmodule // demo"
        ), buffer);
    }

    @Test
    public void closesSingleLineIfBeforeTheNextSiblingStatement() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "if (a)",
                "b <= a;",
                "f <= c;"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new IndentationStyle().applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "if (a)",
                "    b <= a;",
                "f <= c;"
        ), buffer);
    }

    @Test
    public void closesSingleLineIfInsideAlwaysBeforeTheNextStatement() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "module demo;",
                "always @(posedge clk) begin",
                "if (write)",
                "l_reg[l_num] <= data;",
                "led_color <= l_reg[l_cnt];",
                "end",
                "endmodule"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new IndentationStyle().applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    always @(posedge clk) begin",
                "        if (write)",
                "            l_reg[l_num] <= data;",
                "        led_color <= l_reg[l_cnt];",
                "    end",
                "endmodule"
        ), buffer);
    }

    @Test
    public void doesNotIndentTheSecondModuleInTheSameFile() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "module first;",
                "wire a;",
                "endmodule",
                "module second;",
                "wire b;",
                "endmodule"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new IndentationStyle().applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "module first;",
                "    wire a;",
                "endmodule",
                "module second;",
                "    wire b;",
                "endmodule"
        ), buffer);
    }

    @Test
    public void keepsIndentationStableAcrossManySiblingModules() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "module first;",
                "reg a;",
                "endmodule",
                "",
                "module second;",
                "reg b;",
                "endmodule",
                "",
                "module third;",
                "always @(posedge clk)",
                "c <= d;",
                "endmodule"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new IndentationStyle().applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "module first;",
                "    reg a;",
                "endmodule",
                "",
                "module second;",
                "    reg b;",
                "endmodule",
                "",
                "module third;",
                "    always @(posedge clk)",
                "        c <= d;",
                "endmodule"
        ), buffer);
    }
}
