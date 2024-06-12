package uk.co.hopperelec.javacpu.cpu;

enum ByteSizes {
    ONE(1),
    TWO(2),
    FOUR(4),
    EIGHT(8);
    
    final byte size;
    ByteSizes(int size) {
        this.size = (byte)size;
    }
}