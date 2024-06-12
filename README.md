This is a project I and @AzureAqua made during Year 11 (2022) for GCSE Computer Science.
I have made some small changes before publishing this to make the code a bit more readable and to fix a couple minor bugs.

It contains:
- a Java-based representation of a CPU, its key components, and the fetch-decode-execute cycle
- an instruction set based on that of the little man computer.
- an assembler for that instruction set

Everything is designed within the scope of GCSE Computer Science; don't expect anything too advanced!

The CPU represents a 64-bit computer. It has:
- a RAM with configurable size (theoretically up to 2.15GB but only 256 addresses can be fetched)
  - the ability to read machine-code programs (from text files) into memory
    - ignores any characters other than 0 or 1, meaning you can include comments in the machine code
- a CPU with configurable max clock speed (theoretically up to 1GHz, but there are multiple bottlenecks)
  - a control unit handling I/O
    - a decode unit to decode 16-bit instructions from 64-bit data
      - an instruction set with up to 256x 8-bit opcodes (listed in InstructionSet)
  - an ALU capable of addition, subtraction, multiplication, division, modulo and equality
  - the following registers:
    - an accumulator (stores 64-bit values)
    - a program counter (stored 8-bit address for the next instruction to execute)
    - a memory address register (stores 8-bit address currently being fetched from or stored to)
    - a memory data register (stored 64-bit data fetched from RAM)
    - a current instruction register (stored 8-bit opcode of current instruction)
- a motherboard handling communication between the CPU and RAM
  - three buses connecting the RAM and CPU (address bus, data bus and control bus)
    - 16x 4-bit control signals. The First bit indicates read (0) or write (1) and the remaining bits indicate byte size

The assembly language supports the following features:
- comments (started using a `#`)
- variables (created and referenced using `$name`)
  - Optional initial variable values, placed after the variable name
- address references (created and referenced using `-name`)
- `%end` to reference the last address (used to produce arrays)

Currently, the only optimization the assembler makes is that it strips away any 0s from the end of the machine code