package uk.co.hopperelec.javacpu.assembler;

import java.util.Map;

record Variable(String name, int index, String defaultValue) {
    byte parseDefaultValue(Map<String, Byte> addressReferences) {
        if (addressReferences.containsKey(defaultValue))
            return addressReferences.get(defaultValue);
        return Byte.parseByte(defaultValue);
    }

    byte getAddress(int totalInstructions) {
        return (byte) (totalInstructions * 2 + index);
    }

    public String toString() {
        return "Variable " + name + " (" + index + ") with default value " + defaultValue;
    }
}