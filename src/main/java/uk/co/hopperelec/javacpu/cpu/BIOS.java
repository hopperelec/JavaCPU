package uk.co.hopperelec.javacpu.cpu;

import uk.co.hopperelec.javacpu.utils.Utils;

import java.io.IOException;
import java.util.Scanner;

class BIOS {        
    public static void main(String[] args) throws IOException {
        final String path;
        if (args.length == 0) {
            Utils.println("BIOS", "Please enter the path to the machine code you'd like to execute");
            path = new Scanner(System.in).next();
        } else {
            path = args[0];
            if (args.length != 1) Utils.verboseRadix = Byte.parseByte(args[1]);
        }
        Utils.println("BIOS", "Booting", true);
        final Motherboard motherboard = new Motherboard(1024); // 1kHz
        motherboard.ram.loadProgram(path);
        motherboard.cpu.clock.start();
    }
}
