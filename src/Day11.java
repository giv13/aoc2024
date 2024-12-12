import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 {
    private final String filePath;
    private final List<Long> numbers = new ArrayList<>();
    private final Map<Long, List<Long>> cache = new HashMap<>();

    public Day11(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                numbers.addAll(Arrays.stream(line.split("\\s+")).map(Long::parseLong).toList());
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

    public long countNumbers(int times) {
        return numbers.stream().mapToLong(number -> countNumber(number, times)).sum();
    }

    public long countNumber(long number, int times) {
        List<Long> result = cache.get(number);
        if (result != null && times < result.size()) {
            return result.get(times);
        }
        result = new ArrayList<>(List.of(1L));
        if (times > 0) {
            long[] blink = blink(number);
            List<List<Long>> counts = new ArrayList<>();
            for (long num : blink) {
                countNumber(num, times - 1);
                counts.add(cache.get(num));
            }
            for (int i = 0; i < times; i++) {
                long sum = 0;
                for (List<Long> count : counts) {
                    sum += count.get(i);
                }
                result.add(sum);
            }
        }
        cache.put(number, result);
        return result.getLast();
    }

    public long[] blink(long number) {
        if (number == 0) {
            return new long[]{1};
        }
        String numberString = Long.toString(number);
        int numberDigits = numberString.length();
        if (numberDigits % 2 == 0) {
            return new long[]{
                Long.parseLong(numberString.substring(0, numberDigits / 2)),
                Long.parseLong(numberString.substring(numberDigits / 2))
            };
        }
        return new long[]{number * 2024};
    }
}
