package net.ericsonj.verilog.decorations;

import java.util.Arrays;
import java.util.LinkedList;
import net.ericsonj.verilog.FileFormat;
import net.ericsonj.verilog.FormatSetting;
import org.junit.Assert;
import org.junit.Test;

public class ModuleAlignTest {

    @Test
    public void formatsParameterizedModuleInstantiationWithIndentedInstancePorts() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "stretcher #(.WIDTH(21), .PARAM1(23), .PARAM2(55)) link_act_stretcher(",
                ".clk(CLOCK_FIX_50),",
                ".signal_in(led_crs),",
                ".signal_out(LED[2])",
                ");"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new ModuleAlign("stretcher").applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "stretcher #(.WIDTH(21),",
                "            .PARAM1(23),",
                "            .PARAM2(55))",
                "    link_act_stretcher(",
                "        .clk        (CLOCK_FIX_50),",
                "        .signal_in  (led_crs),",
                "        .signal_out (LED[2])",
                "    );"
        ), buffer);
    }

    @Test
    public void detectsAndFormatsModuleInstantiationThroughTheDecoratorEntryPoint() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "stretcher #(.WIDTH(21), .PARAM1(23), .PARAM2(55)) link_act_stretcher(",
                ".clk(CLOCK_FIX_50),",
                ".signal_in(led_crs),",
                ".signal_out(LED[2])",
                ");"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new ModuleInstantiation().applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "stretcher #(.WIDTH(21),",
                "            .PARAM1(23),",
                "            .PARAM2(55))",
                "    link_act_stretcher(",
                "        .clk        (CLOCK_FIX_50),",
                "        .signal_in  (led_crs),",
                "        .signal_out (LED[2])",
                "    );"
        ), buffer);
    }

    @Test
    public void alignsNamedInstancePortParenthesesToTheLongestPortName() {
        LinkedList<String> buffer = new LinkedList<>(Arrays.asList(
                "pos_buffer pos_bufferEx01(",
                ".fifo_rdreq(fifo_rdreq),",
                ".fifo_rddata(fifo_rddata),",
                ".fifo_usedw(fifo_usedw)",
                ");"
        ));

        FileFormat format = new FileFormat(new FormatSetting(null));
        new ModuleInstantiation().applyStyle(format, buffer);

        Assert.assertEquals(Arrays.asList(
                "pos_buffer pos_bufferEx01(",
                "    .fifo_rdreq  (fifo_rdreq),",
                "    .fifo_rddata (fifo_rddata),",
                "    .fifo_usedw  (fifo_usedw)",
                ");"
        ), buffer);
    }
}
