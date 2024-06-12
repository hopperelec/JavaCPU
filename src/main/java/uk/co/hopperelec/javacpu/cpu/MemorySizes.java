package uk.co.hopperelec.javacpu.cpu;

enum MemorySizes {
    BYTES(1),
    KILOBYTES(BYTES.sizeInBytes*1024),
    MEGABYTES(KILOBYTES.sizeInBytes*1024);

    final int sizeInBytes;
    MemorySizes(int sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }
}