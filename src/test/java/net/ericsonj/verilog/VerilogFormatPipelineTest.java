package net.ericsonj.verilog;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class VerilogFormatPipelineTest {

    @Test
    public void formatsControlFlowAssignmentsAndCommentsUsingTheDefaultPipeline() {
        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    always @(posedge clk) begin",
                "        state_wait <= IDLE;",
                "        state      <= RUN; //next",
                "        if (done) begin",
                "            counter    = 0;",
                "            value_long = 1; //set",
                "        end",
                "        else if (error)",
                "            state <= FAIL;",
                "    end",
                "    assign short              = 1;",
                "    assign much_longer_signal = 2; //tail",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                "module demo;",
                "always @(posedge clk)begin",
                "state_wait<=IDLE;",
                "state<=RUN;//next",
                "if(done)begin",
                "counter=0;",
                "value_long=1;//set",
                "end",
                "else if(error)",
                "state<=FAIL;",
                "end",
                "assign short=1;",
                "assign much_longer_signal=2;//tail",
                "endmodule"
        ));
    }

    @Test
    public void formatsCaseBlocksAndEndcaseIndentation() {
        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    always @(posedge clk) begin",
                "        case(state)",
                "            IDLE: begin",
                "                data <= 0;",
                "            end",
                "            default: begin",
                "                data <= 1;",
                "            end",
                "        endcase",
                "    end",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                "module demo;",
                "always @(posedge clk) begin",
                "case(state)",
                "IDLE: begin",
                "data<=0;",
                "end",
                "default: begin",
                "data<=1;",
                "end",
                "endcase",
                "end",
                "endmodule"
        ));
    }

    @Test
    public void formatsModuleDefinitionsAcrossMultipleLines() {
        Assert.assertEquals(Arrays.asList(
                "module demo(",
                "    input clk,",
                "    input rst_n,",
                "    output reg data",
                ");",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                "module demo(input clk,input rst_n,output reg data);",
                "endmodule"
        ));
    }

    @Test
    public void formatsModuleInstantiationsThroughTheDefaultPipeline() {
        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    pos_buffer pos_bufferEx01(",
                "        .fifo_rdreq  (fifo_rdreq),",
                "        .fifo_rddata (fifo_rddata),",
                "        .fifo_usedw  (fifo_usedw)",
                "    );",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                "module demo;",
                "pos_buffer pos_bufferEx01(",
                ".fifo_rdreq(fifo_rdreq),",
                ".fifo_rddata(fifo_rddata),",
                ".fifo_usedw(fifo_usedw)",
                ");",
                "endmodule"
        ));
    }

    @Test
    public void formatsCaseStatementsWhoseLabelsComeFromParameters() {
        Assert.assertEquals(Arrays.asList(
                "module Fibnoacci(",
                "    input clk,",
                "    input reset,",
                "    input in_valid,",
                "    input [7:0]in_level,",
                "    output reg out_valid,",
                "    output reg [7:0]result",
                ");",
                "    ",
                "    parameter [1:0] IDLE = 0, CALCULATE = 1, COMPLETE = 2;",
                "    reg [1:0]Q, Q_NEXT;",
                "    ",
                "    always @(*) begin",
                "        case (Q)",
                "            IDLE:",
                "                if (in_valid)",
                "                    Q_NEXT = CALCULATE;",
                "                else",
                "                    Q_NEXT = IDLE;",
                "            CALCULATE:",
                "                if (done)",
                "                    Q_NEXT = COMPLETE;",
                "                else",
                "                    Q_NEXT = CALCULATE;",
                "            COMPLETE:",
                "                Q_NEXT      = IDLE;",
                "            default: Q_NEXT = IDLE;",
                "        endcase",
                "    end",
                "endmodule // Fibnoacci"
        ), VerilogFormatterTestHelper.formatLines(
                "module Fibnoacci(input clk,",
                "input reset,",
                "input in_valid,",
                "input [7:0]in_level,",
                "output reg out_valid,",
                "output reg [7:0]result);",
                "",
                "parameter [1:0] IDLE = 0, CALCULATE = 1, COMPLETE = 2;",
                "reg [1:0]Q, Q_NEXT;",
                "",
                "always @(*) begin",
                "case (Q)",
                "IDLE:",
                "if (in_valid)",
                "Q_NEXT = CALCULATE;",
                "else",
                "Q_NEXT = IDLE;",
                "CALCULATE:",
                "if (done)",
                "Q_NEXT = COMPLETE;",
                "else",
                "Q_NEXT = CALCULATE;",
                "COMPLETE:",
                "Q_NEXT      = IDLE;",
                "default: Q_NEXT = IDLE;",
                "endcase",
                "end",
                "endmodule // Fibnoacci"
        ));
    }

    @Test
    public void keepsIdentifiersThatStartWithIfIntactInsideIfStatements() {
        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    always @(*) begin",
                "        if (ifmap[6] == 1) begin",
                "            p = p + ({{24{1'b1}}, (ifmap[7]?((~((~(ifmap-1)) << 1))+1):(ifmap<<1))})<<i;",
                "        end else begin",
                "            p = p + ({24'b0, (ifmap[7]?((~((~(ifmap-1)) << 1))+1):(ifmap<<1))})<<i;",
                "        end",
                "        $display(\"\\011 = >2/\");",
                "    end",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                "module demo;",
                "always @(*) begin",
                "if (ifmap[6] == 1) begin",
                "p = p + ({{24{1'b1}}, (ifmap[7]?((~((~(ifmap-1)) << 1))+1):(ifmap<<1))})<<i;",
                "end else begin",
                "p = p + ({24'b0, (ifmap[7]?((~((~(ifmap-1)) << 1))+1):(ifmap<<1))})<<i;",
                "end",
                "$display(\"\\011 = >2/\");",
                "end",
                "endmodule"
        ));
    }

    @Test
    public void keepsKeywordPrefixedIdentifiersIntact() {
        String formatted = String.join("\n", VerilogFormatterTestHelper.formatLines(
                "module demo;",
                "reg else_flag;",
                "reg [3:0] endmodule_cnt;",
                "reg [7:0] input_data;",
                "reg [7:0] module_name;",
                "reg [7:0] case_item;",
                "assign module_name = input_data;",
                "assign case_item = endmodule_cnt[3:0];",
                "if (else_flag)",
                "module_name = case_item;",
                "endmodule"
        ));

        Assert.assertTrue(formatted.contains("else_flag"));
        Assert.assertTrue(formatted.contains("endmodule_cnt"));
        Assert.assertTrue(formatted.contains("input_data"));
        Assert.assertTrue(formatted.contains("module_name"));
        Assert.assertTrue(formatted.contains("case_item"));

        Assert.assertFalse(formatted.contains("else _flag"));
        Assert.assertFalse(formatted.contains("endmodule _cnt"));
        Assert.assertFalse(formatted.contains("input _data"));
        Assert.assertFalse(formatted.contains("module _name"));
        Assert.assertFalse(formatted.contains("case _item"));
    }

    @Test
    public void keepsOuterElseAlignedAfterNestedSingleLineIfElse() {
        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    always@(posedge clk or negedge rstn) begin",
                "        if (!rstn)",
                "            led_reg <= 8'b00000000;",
                "        else if (flag_1s)",
                "            if (led_reg == 8'b00000000)",
                "                led_reg <= 8'b11111110;",
                "            else",
                "                led_reg <= {led_reg[6:0],led_reg[7]};",
                "        else",
                "            led_reg <= led_reg;",
                "    end",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                "module demo;",
                "always@(posedge clk or negedge rstn) begin",
                "if(!rstn)",
                "led_reg<=8'b00000000;",
                "else if(flag_1s)",
                "if(led_reg == 8'b00000000)",
                "led_reg<=8'b11111110;",
                "else",
                "led_reg<={led_reg[6:0],led_reg[7]};",
                "else",
                "led_reg<=led_reg;",
                "end",
                "endmodule"
        ));
    }

    @Test
    public void keepsNestedSingleLineIfElseChainsBoundToTheCorrectElseBranch() {
        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    always@(posedge clk) begin",
                "        if (enable)",
                "            if (up)",
                "                data <= next_data;",
                "            else if (hold)",
                "                data <= data;",
                "            else",
                "                data <= backup_data;",
                "        else",
                "            data <= reset_data;",
                "    end",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                "module demo;",
                "always@(posedge clk) begin",
                "if(enable)",
                "if(up)",
                "data<=next_data;",
                "else if(hold)",
                "data<=data;",
                "else",
                "data<=backup_data;",
                "else",
                "data<=reset_data;",
                "end",
                "endmodule"
        ));
    }

    @Test
    public void preservesComparisonAndAssignmentOperatorsInsideNestedControlFlow() {
        String formatted = String.join("\n", VerilogFormatterTestHelper.formatLines(
                "module demo;",
                "always@(posedge clk) begin",
                "if(enable)",
                "if(product[21:0]!=0)",
                "sticky<=product[22];",
                "else",
                "sticky<=0;",
                "else",
                "sticky<=sticky;",
                "end",
                "endmodule"
        ));

        Assert.assertTrue(formatted.contains("sticky <= product[22];"));
        Assert.assertTrue(formatted.contains("sticky <= 0;"));
        Assert.assertTrue(formatted.contains("product[21:0]!="));

        Assert.assertFalse(formatted.contains("! ="));
        Assert.assertFalse(formatted.contains("< ="));
    }

    @Test
    public void formatsMultipleModuleDefinitionsInOneFileWithoutDriftingIndentation() {
        Assert.assertEquals(Arrays.asList(
                "module sha256_round(",
                "    input [31:0] Kt,",
                "                 Wt,",
                "    input [31:0] a_in,",
                "                 b_in,",
                "                 c_in,",
                "                 d_in,",
                "                 e_in,",
                "                 f_in,",
                "                 g_in,",
                "                 h_in,",
                "    output [31:0] a_out,",
                "                  b_out,",
                "                  c_out,",
                "                  d_out,",
                "                  e_out,",
                "                  f_out,",
                "                  g_out,",
                "                  h_out",
                ");",
                "    ",
                "endmodule",
                "",
                "module sha256_s0(",
                "    input wire [31:0] x,",
                "    output wire [31:0] S0",
                ");",
                "endmodule",
                "",
                "module sha256_s1(",
                "    input wire [31:0] x,",
                "    output wire [31:0] S1",
                ");",
                "endmodule",
                "",
                "module Ch(",
                "    input wire [31:0] x,",
                "                      y,",
                "                      z,",
                "    output wire [31:0] Ch",
                ");",
                "endmodule",
                "",
                "module Maj(",
                "    input wire [31:0] x,",
                "                      y,",
                "                      z,",
                "    output wire [31:0] Maj",
                ");",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                "module sha256_round(",
                "input [31:0] Kt, Wt,",
                "input [31:0] a_in, b_in, c_in, d_in, e_in, f_in, g_in, h_in,",
                "output [31:0] a_out, b_out, c_out, d_out, e_out, f_out, g_out, h_out",
                ");",
                "",
                "endmodule",
                "",
                "module sha256_s0(",
                "input wire [31:0] x,",
                "output wire [31:0] S0",
                ");",
                "endmodule",
                "",
                "module sha256_s1(",
                "input wire [31:0] x,",
                "output wire [31:0] S1",
                ");",
                "endmodule",
                "",
                "module Ch(",
                "input wire [31:0] x, y, z,",
                "output wire [31:0] Ch",
                ");",
                "endmodule",
                "",
                "module Maj(",
                "input wire [31:0] x, y, z,",
                "output wire [31:0] Maj",
                ");",
                "endmodule"
        ));
    }

    @Test
    public void leavesStandaloneCommentLinesUntouched() {
        Assert.assertEquals(Arrays.asList(
                "// ============================================================================",
                "//   Ver  :| Author\t\t\t\t\t:| Mod. Date :| Changes Made:",
                "//   V1.1 :| Alexandra Du\t\t\t:| 06/01/2016:| Added Verilog file",
                "// ============================================================================",
                "",
                "//=======================================================",
                "//  This code is generated by Terasic System Builder",
                "//======================================================="
        ), VerilogFormatterTestHelper.formatLines(
                "// ============================================================================",
                "//   Ver  :| Author\t\t\t\t\t:| Mod. Date :| Changes Made:",
                "//   V1.1 :| Alexandra Du\t\t\t:| 06/01/2016:| Added Verilog file",
                "// ============================================================================",
                "",
                "//=======================================================",
                "//  This code is generated by Terasic System Builder",
                "//======================================================="
        ));
    }

    @Test
    public void leavesParameterlessModuleBodiesOnSeparateLines() {
        Assert.assertEquals(Arrays.asList(
                "module ECN125_receive_position_tb;",
                "    ",
                "    reg sysclk;",
                "    reg reset_n;",
                "    reg encoder_clk;",
                "    int norm_pos_cnt;",
                "    reg UUT_encoder_dir;",
                "    ",
                "    ECN125_receive_position UUT (",
                "        .sysclk  (sysclk),",
                "        .reset_n (reset_n),",
                "        .DEBUG   (GPIO[6:4])",
                "    );",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                "module ECN125_receive_position_tb;",
                "",
                "reg sysclk;",
                "reg reset_n;",
                "reg encoder_clk;",
                "int norm_pos_cnt;",
                "reg UUT_encoder_dir;",
                "",
                "ECN125_receive_position UUT (",
                ".sysclk(sysclk),",
                ".reset_n(reset_n),",
                ".DEBUG(GPIO[6:4]));",
                "endmodule"
        ));
    }

    @Test
    public void preservesCommentsInsideParameterlessModules() {
        Assert.assertEquals(Arrays.asList(
                "`timescale 1ns / 1ps",
                "module stimulus;",
                "    // Inputs",
                "    reg[1:0] x;",
                "    reg[1:0] y;",
                "    // Outputs",
                "    wire z;",
                "    // Instantiate the Unit Under Test (UUT)",
                "    comparator2bit uut (",
                "        .x (x),",
                "        .y (y),",
                "        .z (z)",
                "    );",
                "    ",
                "    initial begin",
                "        $dumpfile(\"test.vcd\");",
                "        $dumpvars(0,stimulus);",
                "        // Initialize Inputs",
                "        x     = 0;",
                "        y     = 0;",
                "        #20 x = 1;",
                "        #20 y = 1;",
                "        #20 y = 3;",
                "        #20 x = 3;",
                "        #20 y = 1;",
                "        #20 y = 0;",
                "        ",
                "        #40 ;",
                "        ",
                "    end",
                "    ",
                "    initial begin",
                "        $monitor(\"t = %3d x = %2b,y = %2b,z = %d \\n\",$time,x,y,z,);",
                "    end",
                "    ",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                "`timescale 1ns / 1ps",
                "module stimulus;",
                "// Inputs",
                "reg[1:0] x;",
                "reg[1:0] y;",
                "// Outputs",
                "wire z;",
                "// Instantiate the Unit Under Test (UUT)",
                "comparator2bit uut (",
                ".x(x),",
                ".y(y),",
                ".z(z)",
                ");",
                "",
                "initial begin",
                "$dumpfile(\"test.vcd\");",
                "$dumpvars(0,stimulus);",
                "// Initialize Inputs",
                "x     = 0;",
                "y     = 0;",
                "#20 x = 1;",
                "#20 y = 1;",
                "#20 y = 3;",
                "#20 x = 3;",
                "#20 y = 1;",
                "#20 y = 0;",
                "",
                "#40 ;",
                "",
                "end",
                "",
                "initial begin",
                "$monitor(\"t = %3d x = %2b,y = %2b,z = %d \\n\",$time,x,y,z,);",
                "end",
                "",
                "endmodule"
        ));
    }

    @Test
    public void honorsSettingsForParenthesesSquareBracketsAndCommentAlignment() throws Exception {
        Path settingsFile = Files.createTempFile("verilog-format", ".properties");
        Files.write(settingsFile, Arrays.asList(
                "SpacesInParentheses=true",
                "SpacesInSquareBrackets=true",
                "AlignLineComments=true",
                "SpacesAfterTrailingComments=1"
        ), StandardCharsets.UTF_8);

        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    always @( posedge clk ) begin // a",
                "        data <= rom[ addr ];      // b",
                "    end",
                "endmodule"
        ), VerilogFormatterTestHelper.formatLines(
                settingsFile.toFile(),
                "module demo;",
                "always @(posedge clk) begin //a",
                "data<=rom[addr];//b",
                "end",
                "endmodule"
        ));
    }

    @Test
    public void formatsAndOverwritesFilesThroughVerilogFile() throws Exception {
        Path source = Files.createTempFile("verilog-format-source", ".v");
        Files.write(source, Arrays.asList(
                "  module demo;  ",
                " always @(posedge clk)",
                " a<=b; ",
                "endmodule "
        ), StandardCharsets.UTF_8);

        VerilogFile file = VerilogFormatterTestHelper.formatFile(source.toString(), (File) null);
        file.overWrite();

        Assert.assertEquals(Arrays.asList(
                "module demo;",
                "    always @(posedge clk)",
                "        a <= b;",
                "endmodule"
        ), Files.readAllLines(source, StandardCharsets.UTF_8));
    }
}
