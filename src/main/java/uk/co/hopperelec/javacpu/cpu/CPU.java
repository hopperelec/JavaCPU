package uk.co.hopperelec.javacpu.cpu;

class CPU {
    final ControlUnit controlUnit;
    final EightByteRegister accumulator = new EightByteRegister();
    final ALU alu = new ALU(accumulator);
    final ByteRegister programCounter = new ByteRegister();
    final ByteRegister mar = new ByteRegister();
    final EightByteRegister mdr = new EightByteRegister();
    final ByteRegister cir = new ByteRegister();
    final Clock clock;

    CPU(Motherboard motherboard, int hertz) {
        controlUnit = new ControlUnit(motherboard, this);
        clock = new Clock(controlUnit, hertz);
    }
}