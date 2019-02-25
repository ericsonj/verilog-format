# Verilog Format

Java console aplication for verilog formatter.

![sample](images/verilog-format.gif) 

## How to use (Linux)

1. Clone repository.

    `$ git clone https://github.com/ericsonj/verilog-format.git`

2. Install verilog-format

    `$ cd verilog-format/bin/`  
    `$ sudo mkdir /opt/verilog-format`  
    `$ sudo unzip verilog-format-LINUX.zip -d /opt/verilog-format/`

3. Execute like java  

    `$ java -jar /opt/verilog-format/verilog-format.jar`

4. Execute like linux script  

    `$ /opt/verilog-format/verilog-format`

5. Install in system

    `$ sudo cp /opt/verilog-format/verilog-format /usr/bin/`

## How to use (Windows)

1. Clone repository or download [verilog-format-WIN.zip](bin/verilog-format-WIN.zip)  

2. Unzip and copy in your preferer folder.

## Build project

For build de project, Maven is needed.  

`$ cd verilog-format`  
`$ mvn clean package`  
`$ ls target/`  
