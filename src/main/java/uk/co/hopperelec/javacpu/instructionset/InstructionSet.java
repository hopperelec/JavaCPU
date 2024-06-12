package uk.co.hopperelec.javacpu.instructionset;

import uk.co.hopperelec.javacpu.utils.Utils;

import java.lang.IllegalArgumentException;
import java.util.stream.Stream;
import java.util.Map;

public enum InstructionSet {
    HLT, // 00000000 | Halt entire processor until an interrupt signal is received
    ADD(OperandPart.ADDRESS), // 00000001 | Add value from RAM at 'address' to accumulator
    SUB(OperandPart.ADDRESS), // 00000010 | Subtract value from RAM at 'address' to accumulator
    INC(OperandPart.RAW_DATA), // 00000011 | Add 'raw data' to accumulator
    DEC(OperandPart.RAW_DATA), // 00000100 | Subtract 'raw data' from accumulator
    MUL(OperandPart.ADDRESS), // 00000101 | Multiply value in accumulator by value from RAM at 'address'
    DIV(OperandPart.ADDRESS), // 00000110 | Divide value in accumulator by value from RAM at 'address'
    MOD(OperandPart.ADDRESS), // 00000111 | Set accumulator to the modulus of the accumulator divided by value in RAM at 'address'
    LDA(OperandPart.ADDRESS), // 00001000 | Load value from RAM at 'address' to accumulator
    STO(OperandPart.ADDRESS), // 00001001 | Store value (stored in accumulator) in RAM at 'address'
    STA(OperandPart.ADDRESS), // 00001010 | Store value (stored in accumulator) in RAM at address stored in RAM at 'address'
    INP(OperandPart.NUM_BYTES, OperandPart.radix(5)), // 00001011 | Store user-inputted value in accumulator
    OUT(OperandPart.radix(8)), // 00001100 | Output value stored in accumulator
    BAA(OperandPart.ADDRESS), // 00001101 | Absolute branch always (set program counter to 'address')
    BRA(OperandPart.ADDRESS), // 00001110 | Relative branch always (add 'address' to program counter)
    BAC(OperandPart.ADDRESS), // 00001111 | Absolute equality conditional branch (set program counter to 'address' if accumulator is 0)
    BRC(OperandPart.ADDRESS), // 00010000 | Relative equality conditional branch (add 'address' to program counter if accumulator is 0)
    BAD(OperandPart.ADDRESS), // 00010001 | Absolute negative conditional branch (set program counter to 'address' if accumulator less than 0)
    BRD(OperandPart.ADDRESS); // 00010010 | Relative negative conditional branch (add 'address' to program counter if accumulator less than 0)
    
    public byte opcode() {
        return (byte) ordinal();
    }

    public final OperandPart[] operandParts;
    InstructionSet(OperandPart... operandParts) {
        if (Stream.of(operandParts).mapToInt(operandPart -> operandPart.bits).sum() > 8)
            throw new IllegalArgumentException("Operands are only 8 bits long but parts given require more than 8 bits in total");
        this.operandParts = operandParts;
    }

    public byte parseOperand(String[] instructionParts, Map<String,Byte> addressReferences) {
        byte operand = 0;
        byte i = 1;
        for (OperandPart operandPart : operandParts) {
            final String partString = instructionParts[i++];
            byte partByte;
            if (operandPart.bits == 8 && addressReferences.containsKey(partString)) {
                partByte = addressReferences.get(partString);
            } else {
                partByte = Byte.parseByte(partString, operandPart.radix);
                if (operandPart.zeroIndexed) partByte--;
                int bitsRequired = Byte.SIZE-Integer.numberOfLeadingZeros(partByte);
                if (bitsRequired > operandPart.bits)
                    throw new IllegalArgumentException("Value given for "+operandPart.name+" requires "+bitsRequired+" bits to store but only "+operandPart.bits+" are allowed");
            }
            operand = (byte)((operand<<operandPart.bits)+partByte);
        }
        return operand;
    }

    public short produceInstruction(byte operand) {
        return (short)((ordinal()<<8)+Utils.unsignByte(operand));
    }
    public short produceInstruction(String[] instructionParts, Map<String,Byte> addressReferences) {
        return produceInstruction(parseOperand(instructionParts, addressReferences));
    }
}