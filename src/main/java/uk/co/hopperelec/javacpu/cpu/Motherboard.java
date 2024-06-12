package uk.co.hopperelec.javacpu.cpu;

import uk.co.hopperelec.javacpu.utils.Utils;

import java.lang.IllegalStateException;
    
class Motherboard {
    final CPU cpu;
    final RAM ram = new RAM((short)256, MemorySizes.BYTES);
    private Byte addressBuffer = null;
    private Byte controlBuffer = null;

    Motherboard(int hertz) {
        cpu = new CPU(this, hertz);
    }

    boolean controlSignalWrite(byte controlSignal) {
        return (controlSignal & 8) != 0;
    }
    byte controlSignalSize(byte controlSignal) {
        return ByteSizes.values()[controlSignal & 7].size;
    }
    
    Bus addressBus = new Bus() {
        void toRAM(byte address) {
            Utils.println("Motherboard", "Transferring {0} to RAM over address bus", true, address);
            if (controlBuffer != null)
                throw new IllegalStateException("Memory address sent while control signal still active! There may be another instruction still being executed");
            addressBuffer = address;
        }
    };
    
    Bus dataBus = new Bus() {
        void toRAM(long data) {
            Utils.println("Motherboard", "Transferring {0} to RAM over data bus", true, data);
            if (controlBuffer == null || !controlSignalWrite(controlBuffer))
                throw new IllegalStateException("CPU sent data over data bus without sending 'STA' control signal");
            ram.putData(data, addressBuffer, controlSignalSize(controlBuffer));
            controlBuffer = null;
            addressBuffer = null;
        }

        void toCPU(long data) {
            Utils.println("Motherboard", "Transferring {0} to CPU over data bus", true, data);
            cpu.mdr.storedData = data;
            cpu.controlUnit.handleData();
        }
    };
    
    Bus controlBus = new Bus() {
        void toRAM(byte controlSignal) {
            Utils.println("Motherboard", "Transferring {0} to RAM over control bus", true, controlSignal);
            if (addressBuffer == null) throw new IllegalStateException("Control signal received before address");
            if (controlSignalWrite(controlSignal)) {
                controlBuffer = controlSignal;
            } else {
                dataBus.toCPU(ram.getData(addressBuffer, controlSignalSize(controlSignal)));
                addressBuffer = null;
            }
        }

        void toCPU(byte data) {
            Utils.println("Motherboard", "Transferring {0} to CPU over control bus", true, data);
            // This could be used in future for interrupt signals
        }
    };
}