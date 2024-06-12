package uk.co.hopperelec.javacpu.cpu;

import uk.co.hopperelec.javacpu.utils.Utils;
import uk.co.hopperelec.javacpu.instructionset.InstructionSet;

import java.util.Scanner;

class ControlUnit {
    private final Motherboard motherboard;
    private final CPU cpu;
    private final Scanner scanner = new Scanner(System.in);
    final DecodeUnit decodeUnit = new DecodeUnit();

    ControlUnit(Motherboard motherboard, CPU cpu) {
        this.motherboard = motherboard;
        this.cpu = cpu;
    }
    
    void startNewCycle() {
        cpu.mar.storedData = cpu.programCounter.storedData;
        cpu.programCounter.storedData += 2;
        cpu.cir.storedData = InstructionSet.BAA.opcode();
        motherboard.addressBus.toRAM(cpu.mar.storedData);
        motherboard.controlBus.toRAM(encodeControlSignal(false, ByteSizes.TWO)); // Read opcode and operand
    }

    private byte encodeControlSignal(boolean write, ByteSizes size) {
        return (byte)((write ? 8 : 0)+size.ordinal());
    }

    private void store(byte address, ByteSizes size) {
        cpu.mar.storedData = address;
        motherboard.addressBus.toRAM(cpu.mar.storedData);
        motherboard.controlBus.toRAM(encodeControlSignal(true, size));
        motherboard.dataBus.toRAM(cpu.accumulator.storedData);
    }

    void handleData() {
        final InstructionSet triggeringInstruction = decodeUnit.decodeOpcode(cpu.cir.storedData);
        Utils.println("Control Unit", "Received {0} triggered by instruction "+triggeringInstruction, true, cpu.mdr.storedData);
        if (triggeringInstruction == InstructionSet.BAA) {
            Utils.println("Control Unit", "Processing received data as an instruction", true);
            cpu.cir.storedData = decodeUnit.readOpcode(cpu.mdr.storedData);
            final InstructionSet fetchedInstruction = decodeUnit.decodeOpcode(cpu.cir.storedData);
            Utils.println("Control Unit", "Instruction type (opcode): "+fetchedInstruction, true);
            final byte operand = decodeUnit.readOperand(cpu.mdr.storedData);
            Utils.println("Control Unit", "Instruction operand: {0}", true, operand);
            switch (fetchedInstruction) {
                case HLT:
                    cpu.clock.halt();
                    Utils.println("Control Unit", "Halted");
                    break;
                    
                case ADD, SUB, MUL, DIV, MOD, LDA, STA:
                    cpu.mar.storedData = operand;
                    motherboard.addressBus.toRAM(cpu.mar.storedData);
                    motherboard.controlBus.toRAM(encodeControlSignal(false, ByteSizes.ONE));
                    break;

                case INC:
                case DEC:
                    cpu.alu.handleData(fetchedInstruction, operand);
                    break;

                case STO:
                    store(operand, ByteSizes.ONE);
                    break;

                case INP:
                    cpu.clock.halt();
                    final byte maxBytes = (byte)(((operand>>5)&7)+1); // First 3 bits
                    final long maxValue = Utils.bytesToMaxValue(maxBytes);
                    final long minValue = -maxValue-1;
                    final byte radixIn = (byte)((operand&31)+1); // Last 5 bits
                    Utils.println("Input", "Please input a number between "+Long.toString(minValue, radixIn)+" and "+Long.toString(maxValue, radixIn)+" using a radix of "+radixIn);
                    while (true) {
                        String input = scanner.next();
                        try {
                            cpu.accumulator.storedData = Long.parseLong(input, radixIn);
                        } catch (NumberFormatException e) {
                            Utils.println("Input", "Input incorrectly formatted, please try again.");
                            continue;
                        }
                        if (cpu.accumulator.storedData >= minValue && cpu.accumulator.storedData <= maxValue) break;
                        Utils.println("Input", "Input not in required range, please try again.");
                    }
                    cpu.clock.start();
                    break;

                case OUT:
                    final byte radixOut = (byte)((operand&0x7F)+1);
                    Utils.println("Output", "OUTPUTTED (radix "+radixOut+"): "+Long.toString(cpu.accumulator.storedData, radixOut));
                    break;

                case BAA:
                    cpu.programCounter.storedData = operand;
                    break;

                case BRA:
                    cpu.programCounter.storedData += operand;
                    break;
                    
                case BAC:
                    if (cpu.alu.equals())
                        cpu.programCounter.storedData = operand;
                    break;
                    
                case BRC:
                    if (cpu.alu.equals())
                        cpu.programCounter.storedData += operand;
                    break;
                    
                case BAD:
                    if (cpu.alu.negative())
                        cpu.programCounter.storedData = operand;
                    break;
                    
                case BRD:
                    if (cpu.alu.negative())
                        cpu.programCounter.storedData += operand;
                    break;
            }
        } else if (triggeringInstruction == InstructionSet.STA) {
            Utils.println("Control Unit", "Received address to store data to", true);
            store((byte)cpu.mdr.storedData, ByteSizes.ONE);
        } else {
            Utils.println("Control Unit", "Received data is not an instruction or address; handing data to ALU", true);
            cpu.alu.handleData(triggeringInstruction, cpu.mdr.storedData);
        }
    }
}