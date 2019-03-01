/**
 * File:   genrom.v
 * Author: Ericson Joseph
 *
 * Created on January 28, 2019,  8:15 PM
 */

module genrom #(parameter AW = 5,
                parameter DW = 4)
               (input clk,
                input wire [AW-1:0] addr,
                output reg [DW-1:0] data);
    
    //holaa comentario
    
    parameter ROMFILE = "rom1.list";
    
    localparam NPOS = 2 ** AW;
    
    reg [DW-1:0]         rom [0:NPOS-1];
    
    always @(posedge clk) begin
        data <= rom[addr];
    end
    
    initial begin
        $readmemh(ROMFILE, rom);
    end
    
    
endmodule
