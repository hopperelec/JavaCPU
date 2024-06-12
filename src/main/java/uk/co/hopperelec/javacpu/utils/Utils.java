package uk.co.hopperelec.javacpu.utils;

import java.text.MessageFormat;
import java.util.Arrays;

public class Utils {
    public static byte verboseRadix = 10;

    public static String toBinary(long unsignedData, int bits) {
        return padBinary(Long.toBinaryString(unsignedData), bits, true);
    }

    public static String toBinary(byte signedData, int bits) {
        return toBinary(unsignByte(signedData), bits);
    }

    public static byte signByte(short unsignedByte) {
        return (byte)(unsignedByte > 0x7F ? unsignedByte-0x100 : unsignedByte);
    }

    public static short unsignByte(byte signedByte) {
        return (short)(signedByte < 0 ? signedByte+0x100 : signedByte);
    }

    public static String padBinary(String binary, int bits, boolean left) {
        return String.format("%"+(left ? "" : "-")+bits+"s", binary).replace(' ', '0');
    }

    // Keep in mind that this does not pad the string with 0s
    public static byte parseByteString(String byteString) {
        return signByte(Short.parseShort(byteString, 2));
    }

    public static void println(String part, String message, boolean verbose, long... numbers) {
        if (verbose && verboseRadix <= 0) return;
        message = "["+part+"] "+MessageFormat.format(
                message,
                Arrays.stream(numbers).mapToObj(
                        number -> Long.toString(number, verboseRadix)+" (radix "+verboseRadix+")"
                ).toArray(Object[]::new)
        );
        if (!verbose && verboseRadix > 0) message = "* "+message;
        System.out.println(message);
    }
    public static void println(String part, String message, long... numbers) {
        println(part, message, false, numbers);
    }

    public static long bytesToMaxValue(byte bytes) {
        if (bytes > 8) throw new IllegalArgumentException("Can only store the value of up to 8 bytes");
        byte bits = (byte)(bytes*8-1);
        long result = 2;
        while (bits > 1) {
            result *= 2;
            bits--;
        }
        return result-1;
    }
}