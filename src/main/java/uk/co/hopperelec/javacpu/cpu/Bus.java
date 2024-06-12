package uk.co.hopperelec.javacpu.cpu;

abstract class Bus {
    void toRAM(byte data) {}
    void toRAM(long data) {}
    void toCPU(byte data) {}
    void toCPU(long data) {}
}