import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day17 {
    private final String filePath;
    private final Map<Character, Long> registers = new HashMap<>();
    private int[] program;

    public Day17(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            Pattern pattern = Pattern.compile("(?:Register|Program) ?([ABC]?): ([\\d,]+)");
            Matcher matcher;
            while ((line = reader.readLine()) != null) {
                matcher = pattern.matcher(line);
                if (matcher.find()) {
                    if (matcher.group(1).isEmpty()) {
                        program = Arrays.stream(matcher.group(2).split(",")).mapToInt(Integer::parseInt).toArray();
                    } else {
                        registers.put(matcher.group(1).charAt(0), Long.valueOf(matcher.group(2)));
                    }
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        } catch (NumberFormatException e) {
            System.out.println("The string is not a number!");
        }
    }

    public String getProgramOutput() {
        List<String> output = new ArrayList<>();
        int pointer = 0;
        while (pointer < program.length - 1) {
            Integer result = operation(program[pointer], program[pointer + 1]);
            if (result != null) {
                if (program[pointer] == 3) {
                    pointer = result;
                    continue;
                } else {
                    output.add(String.valueOf(result));
                }
            }
            pointer += 2;
        }
        return String.join(",", output);
    }

    public Long getLowestValue() {
        return getLowestValue(0, 1);
    }

    private Long getLowestValue(long from, int level) {
        String prog = String.join(",", Arrays.stream(Arrays.copyOfRange(program, program.length - level, program.length)).mapToObj(String::valueOf).toList());
        for (long i = from; i < from + 8; i++) {
            registers.put('A', i);
            String output = getProgramOutput();
            if (output.equals(prog)) {
                if (level == program.length) {
                    return i;
                }
                Long lowestValue = getLowestValue(i * 8, level + 1);
                if (lowestValue != null) {
                    return lowestValue;
                }
            }
        }
        return null;
    }

    private Integer operation(int opcode, int operand) {
        Integer result = null;
        switch (opcode) {
            case 1 -> registers.put('B', registers.get('B') ^ operand);
            case 2 -> registers.put('B', getComboOperand(operand) % 8);
            case 3 -> result = registers.get('A') == 0 ? null : operand;
            case 4 -> registers.put('B', registers.get('B') ^ registers.get('C'));
            case 5 -> result = (int) (getComboOperand(operand) % 8);
            case 0, 6, 7 -> {
                char register = 'A';
                if (opcode == 6) {
                    register = 'B';
                } else if (opcode == 7) {
                    register = 'C';
                }
                registers.put(register, registers.get('A') / (1L << getComboOperand(operand)));
            }
        }
        return result;
    }

    private long getComboOperand(int operand) {
        switch (operand) {
            case 4 -> {
                return registers.get('A');
            }
            case 5 -> {
                return registers.get('B');
            }
            case 6 -> {
                return registers.get('C');
            }
        }
        return operand;
    }
}
