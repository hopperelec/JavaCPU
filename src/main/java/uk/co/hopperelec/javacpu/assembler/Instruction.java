package uk.co.hopperelec.javacpu.assembler;

import uk.co.hopperelec.javacpu.instructionset.InstructionSet;
import uk.co.hopperelec.javacpu.utils.Utils;

import java.util.Map;

class Instruction {
    final String instructionString;
    final InstructionSet opcode;
    final String[] parts;

    Instruction(String instructionString) {
        this.instructionString = instructionString;
        parts = instructionString.split(" ");
        opcode = InstructionSet.valueOf(parts[0]);
    }

    String generateMachineCode(Map<String,Byte> addressReferences) {
        return Utils.toBinary(opcode.produceInstruction(parts, addressReferences), 16);
    }

    public String toString() {
        return opcode+" "+String.join(" ", parts);
    }
}