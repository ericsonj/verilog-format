/**
 * File:   genrom.v
 * Author: Ericson Joseph
 * //test
 * Created on January 28, 2019,  8:15 PM
 */

module genrom #(parameter AW = 5,          // Address Width
                parameter DW = 4)          // Data width
               (input clk,
                input wire [AW-1:0] addr,
                output reg [DW-1:0] data);
    
    // holaa comentario
    // otro comentario
    
    parameter ROMFILE = "rom1.list"; // COMENTARIO 1
    localparam NPOS   = 2 ** AW;
    
    reg [DW-1:0] rom [0:NPOS-1];
    
    always @( posedge clk ) begin
        data     <= rom[addr];
        data_new <= rom[addr_new];
        data3    <= rom[0:4];
        
        if ( a == 0 )      // IF COMMENT
        begin              // BEGIN COMMENT
            a       = 2;   // 
            numberA = 2;   // 
        end                // END commnet
        else if ( b == 4 ) // COMMNET ELSE IF
        begin              // COMMNET ELSE IF
            a <= 55;       // 
        end                // 
        else begin         // ELSE COMMNET
                           // COMMNET ELSE
            a <= 3;        // 
        end                // 
        
    end
    
    localparam IDLE  = 0; // IDLE
    localparam START = 1; // START
    localparam TRANS = 2; // TRANS
    
    assign load    = ( state == START ) ? 1 : 0;
    assign baud_en = ( state == IDLE ) ? 0 : 1;
    
    initial begin
        $readmemh( ROMFILE, rom );
    end
    
    
endmodule
