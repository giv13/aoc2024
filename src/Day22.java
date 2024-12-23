import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22 {
    private final String filePath;
    private final List<Integer> numbers = new ArrayList<>();

    public Day22(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.add(Integer.valueOf(line));
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

    public long getSum(int iterations) {
        return numbers.stream().mapToLong(n -> generateNumbers(new ArrayList<>(List.of((long) n)), iterations).getLast()).sum();
    }

    public int getBananas(int iterations) {
        Map<String, List<Integer>> result = new HashMap<>();
        for (Integer number : numbers) {
            Map<String, Integer> sequences = new HashMap<>();
            List<Integer> numbers = generateNumbers(new ArrayList<>(List.of((long) number)), iterations).stream().map(n -> (int) (n % 10)).toList();
            for (int i = 4; i < numbers.size(); i++) {
                StringBuilder sequence = new StringBuilder();
                for (int j = 3; j >= 0; j--) {
                    sequence.append(numbers.get(i - j) - numbers.get(i - j - 1));
                    if (j == 0 && !sequences.containsKey(sequence.toString())) {
                        sequences.put(sequence.toString(), numbers.get(i));
                    }
                }
            }
            for (Map.Entry<String, Integer> seq : sequences.entrySet()) {
                result.computeIfAbsent(seq.getKey(), p -> new ArrayList<>()).add(seq.getValue());
            }
        }
        return result.values().stream().mapToInt(p -> p.stream().mapToInt(Integer::intValue).sum()).max().orElse(0);
    }

    private List<Long> generateNumbers(List<Long> numbers, int iterations) {
        if (iterations == 0) {
            return numbers;
        }
        long number = numbers.getLast();
        number = calculateNumber(number, number * 64);
        number = calculateNumber(number, number / 32);
        number = calculateNumber(number, number * 2048);
        numbers.add(number);
        return generateNumbers(numbers, iterations - 1);
    }

    private long calculateNumber(long number, long newNumber) {
        return (number ^ newNumber) % 16777216;
    }
}
