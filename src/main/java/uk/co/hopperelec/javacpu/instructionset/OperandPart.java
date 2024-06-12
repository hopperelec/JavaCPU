package uk.co.hopperelec.javacpu.instructionset;

public class OperandPart {
    public final String name;
    public final boolean zeroIndexed;
    public final byte bits;
    public final byte radix;

    public OperandPart(String name, boolean zeroIndexed, int bits, int radix) {
        this.name = name;
        this.zeroIndexed = zeroIndexed;
        this.bits = (byte) bits;
        this.radix = (byte) radix;
    }
    public OperandPart(String name, boolean zeroIndexed, int bits) {
        this(name, zeroIndexed, bits, 2);
    }
    public OperandPart(String name, boolean zeroIndexed) {
        this(name, zeroIndexed, 8);
    }
    public OperandPart(String name) {
        this(name, false);
    }

    public static final OperandPart ADDRESS = new OperandPart("address");
    public static final OperandPart RAW_DATA = new OperandPart("raw data", false, 8, 10);
    public static final OperandPart NUM_BYTES = new OperandPart("number of bytes", true, 3, 10);
    public static OperandPart radix(int bits) {
        return new OperandPart("radix", true, bits, 10);
    }
}