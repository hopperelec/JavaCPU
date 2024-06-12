package uk.co.hopperelec.javacpu.cpu;

import uk.co.hopperelec.javacpu.instructionset.InstructionSet;
import uk.co.hopperelec.javacpu.utils.Utils;

import java.lang.IllegalArgumentException;

class DecodeUnit {
    byte instructionSetLength = (byte) InstructionSet.values().length;
    InstructionSet decodeOpcode(byte opcode) throws IllegalArgumentException {
        Utils.println("Decode Unit", "Decoding opcode {0}", true, opcode);
        final short index = Utils.unsignByte(opcode);
        if (index >= instructionSetLength)
            throw new IllegalArgumentException("opcode too large (only "+instructionSetLength+" instructions exist)");
        return InstructionSet.values()[index];
    }
    InstructionSet decodeOpcode(long opcode) {
        return decodeOpcode(readOpcode(opcode));
    }

    byte readOpcode(long data) {
        return (byte)((data&0xff00)>>8);
    }
    byte readOperand(long data) {
        return (byte)(data&0xff);
    }
}