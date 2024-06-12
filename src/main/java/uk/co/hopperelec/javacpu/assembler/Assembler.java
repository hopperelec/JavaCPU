package uk.co.hopperelec.javacpu.assembler;

import uk.co.hopperelec.javacpu.utils.Utils;

import java.io.IOException;
import java.util.Scanner;

class Assembler {
    public static void main(String[] args) throws IOException {
        final String path;
        if (args.length == 0) {
            Utils.println("Assembler", "Please enter the path to the assembly you'd like to assemble");
            path = new Scanner(System.in).next();
        } else {
            path = args[0];
        }
        Utils.println("Assembler", Assembly.fromFile(path).generateMachineCode());
    }
}