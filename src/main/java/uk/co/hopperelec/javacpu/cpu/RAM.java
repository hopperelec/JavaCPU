package uk.co.hopperelec.javacpu.cpu;

import uk.co.hopperelec.javacpu.utils.Utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class RAM {
    private final byte[] addresses;
    private static final Pattern bytePattern = Pattern.compile("[01]{8}");

    RAM(short size, MemorySizes unit) {
        addresses = new byte[size*unit.sizeInBytes];
    }
    
    void loadProgram(String filename) throws IOException {
        Utils.println("RAM", "Reading program '"+filename+"'", true);
        final String machineCode = Files.readString(Paths.get(filename))
                .replaceAll("[^01]", ""); // Remove non-binary characters
        final Matcher byteMatcher = bytePattern.matcher(machineCode);
        int i = 0;
        while (byteMatcher.find()) {
            addresses[i++] = Utils.parseByteString(byteMatcher.group());
        }
        if (i*8 != machineCode.length()) {
            addresses[i] = Utils.parseByteString(Utils.padBinary(machineCode.substring(i*8), 8, false));
        }
    }

    long getData(byte address, byte size) {
        Utils.println("RAM", "Getting {0} bytes of data from address {1}", true, size, address);
        final short index = Utils.unsignByte(address);
        if (size == 1) return addresses[index];
        long value = 0;
        for (int i = 0; i < size; i++) {
            value = (value << 8)+Utils.unsignByte(addresses[index+i]);
        }
        return value;
    }

    void putData(long data, byte address, byte size) {
        Utils.println("RAM", "Storing {0} in address {1} across {2} bytes", true, data, address, size);
        final short index = Utils.unsignByte(address);
        for (int i = 0; i < size; i++) {
            addresses[index+i] = (byte)(data >> (i*8));
        }
    }
}