/**
 * File:   genrom.v
 * Author: Ericson Joseph
 * //test
 * Created on January 28, 2019,  8:15 PM
 */

module genrom #(parameter AW = 5,          // Address Width
                parameter DW = 4)
               (input clk,
                input wire [ AW-1:0 ] addr,
                output reg [ DW-1:0 ] data);
    
    // holaa comentario
    
    parameter ROMFILE = "rom1.list"; // COMENTARIO 1
    
    localparam NPOS = 2 ** AW;
    
    reg [ DW-1:0 ] rom [0:NPOS-1];
    
    always @(posedge clk) begin 
        data <= rom[addr];
        
        if (a == 0) // IF COMMENT
        // COMMENT TEST
        begin // BEGIN COMMENT
            a = 2; //
        end // END commnet
        else if (b == 4) // COMMNET ELSE IF
        // COMMNET ELSE IF
        begin //
            a = 55; //
        end//
        else begin // ELSE COMMNET
            // COMMNET ELSE
            a = 3;//
        end//
        
    end
    
    initial begin
        $readmemh(ROMFILE, rom);
    end
    
    
endmodule
