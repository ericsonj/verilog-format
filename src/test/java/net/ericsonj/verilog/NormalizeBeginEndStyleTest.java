package net.ericsonj.verilog;

import java.util.Arrays;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Test;

public class NormalizeBeginEndStyleTest {

    @Test
    public void normalizesInlineBeginAndEndBeforeIndentation() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "always @(posedge clk)begin",
                "pre_btn2 <= btn_out[2];",
                "if (pre_btn2 == 1'b0&&btn_out[2] == 1'b1) begin",
                "disp_num[11:8] <= disp_num[11:8]+1'b1;",
                "end",
                "if (disp_num[11:8] == 4'b1010)begin",
                "disp_num[11:8] <= 4'b0;end",
                "end"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new NormalizeBeginEndStyle().applyStyle(format, buffer);
        new IndentationStyle().applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "always @(posedge clk) begin",
                "    pre_btn2 <= btn_out[2];",
                "    if (pre_btn2 == 1'b0&&btn_out[2] == 1'b1) begin",
                "        disp_num[11:8] <= disp_num[11:8]+1'b1;",
                "    end",
                "    if (disp_num[11:8] == 4'b1010) begin",
                "        disp_num[11:8] <= 4'b0;",
                "    end",
                "end"
        ), buffer);
    }

    @Test
    public void keepsEndElseBeginOnTheSameLineWhileIndentingItCorrectly() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "if (a) begin",
                "b <= 1;",
                "end else begin",
                "b <= 0;",
                "end"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new NormalizeBeginEndStyle().applyStyle(format, buffer);
        new IndentationStyle().applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "if (a) begin",
                "    b <= 1;",
                "end else begin",
                "    b <= 0;",
                "end"
        ), buffer);
    }
}
