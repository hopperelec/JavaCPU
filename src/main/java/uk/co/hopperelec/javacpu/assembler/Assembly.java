package uk.co.hopperelec.javacpu.assembler;

import uk.co.hopperelec.javacpu.utils.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

class Assembly {
    final Map<String, Byte> addressReferences = new HashMap<>();
    final List<Variable> variables = new ArrayList<>();
    final List<Instruction> instructions = new ArrayList<>();
    
    Assembly(String assembly) {
        parse(assembly);
    }

    static Assembly fromFile(String filename) throws IOException {
        return new Assembly(Files.readString(Paths.get(filename)));
    }

    void parse(String assembly) {
        Utils.println("Parser", "Parsing file for instructions, variables and branch references", true);
        for (String line : assembly.split("\n")) {
            parseLine(line);
        }
        addressReferences.put("%end", (byte)(variables.size()+instructions.size()*2));
        for (Variable variable : variables) {
            addressReferences.put(variable.name(), variable.getAddress(instructions.size()));
        }
    }

    void parseLine(String line) {
        line = line.replaceAll("#.*", "").strip();
        if (line.isEmpty()) return;
        if (line.startsWith("-")) {
            Utils.println("Parser", "Reference '"+line+"' found to address {0}", true, instructions.size());
            addressReferences.put(line, (byte)(instructions.size()*2));
        } else if (line.startsWith("$")) {
            final String[] parts = line.split(" ");
            final String defaultValue = parts.length == 1 ? "0" : parts[1];
            Utils.println("Parser", "Reference '"+parts[0]+"' found to variable with default value "+defaultValue, true);
            variables.add(new Variable(parts[0], variables.size(), defaultValue));
        } else {
            Utils.println("Parser", "Line is not a variable or reference; parsing it as instruction", true);
            final Instruction instruction = new Instruction(line);
            instructions.add(instruction);
            Utils.println("Parser", "Parsed instruction "+instruction, true);
        }
        Utils.println("Parser", "", true);
    }

    String generateMachineCode() {
        Utils.println("Generator", "Generating machine code", true);
        final StringBuilder machineCodeBuilder = new StringBuilder();
        for (Instruction instruction : instructions) {
            Utils.println("Generator", "Assembling instruction: "+instruction.instructionString, true);
            final String result = instruction.generateMachineCode(addressReferences);
            machineCodeBuilder.append(result);
            Utils.println("Generator", "Generated machine code "+result, true);
        }
        for (Variable variable : variables) {
            final String defaultValue = Utils.toBinary(variable.parseDefaultValue(addressReferences), 8);
            Utils.println("Generator", "Appended "+variable+" as "+defaultValue, true);
            machineCodeBuilder.append(defaultValue);
        }
        return machineCodeBuilder.toString()
            .replaceFirst("0+$", ""); // Remove redundant trailing zeroes
    }
}