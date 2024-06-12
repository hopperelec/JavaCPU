package uk.co.hopperelec.javacpu.cpu;

import uk.co.hopperelec.javacpu.instructionset.InstructionSet;
import uk.co.hopperelec.javacpu.utils.Utils;

import java.lang.IllegalArgumentException;
    
class ALU {
    private final EightByteRegister accumulator;

    ALU(EightByteRegister accumulator){
        this.accumulator = accumulator;
    }

    void handleData(InstructionSet instruction, long data) {
        final long originalACC = accumulator.storedData;
        switch (instruction) {
            case ADD:
            case INC:
                accumulator.storedData += data;
                break;
            case SUB:
            case DEC:
                accumulator.storedData -= data;
                break;
            case LDA:
                accumulator.storedData = data;
                break;
            case MUL:
                accumulator.storedData *= data;
                break;
            case DIV:
                accumulator.storedData /= data;
                break;
            case MOD:
                accumulator.storedData %= data;
                break;
            default:
                throw new IllegalArgumentException("ALU called for non-arithmetic instruction");
        }
        Utils.println("ALU", originalACC+" "+instruction+" "+data+" = "+accumulator.storedData, true);
    }

    boolean equals() {
        return accumulator.storedData == 0;
    }
    boolean negative() {
        return accumulator.storedData < 0;
    }
}