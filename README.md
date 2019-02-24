# Verilog Format

Java console aplication for verilog formatter.

![](https://github.com/ericsonj/verilog-format/blob/master/images/verilog-format.gif) 

## How to use (Linux)

1. Clone repository.

    `$ git clone https://github.com/ericsonj/verilog-format.git`

2. Copy files in bin/ to /opt/verilog-format/

    `$ cd verilog-format`  
    `$ sudo mkdir /opt/verilog-format`  
    `$ sudo cp bin/* /opt/verilog-format/`

3. Execute like java  

    `$ java -jar /opt/verilog-format/verilog-format.jar`

4. Exceute like linux script  

    `$ /opt/verilog-format/verilog-format`

5. Install in system

    `$ sudo cp /opt/verilog-format/verilog-format /usr/bin/`

## How to use (Windows)

TODO


## Build project

For build de project, Maven is needed.  

`$ mvn clean compile assembly:single`  
`$ cp target/verilog-format-1.0.0-full.jar bin/verilog-format-1.0.0.jar`
