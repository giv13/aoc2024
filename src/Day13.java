import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {
    private final String filePath;
    private final List<List<Integer>> equations = new ArrayList<>();

    public Day13(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            Pattern pattern = Pattern.compile("(?:Button [AB]|Prize): X[+=](\\d+), Y[+=](\\d+)");
            Matcher matcher;
            List<Integer> numbers = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    equations.add(numbers);
                    numbers = new ArrayList<>();
                } else {
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        numbers.add(Integer.parseInt(matcher.group(1)));
                        numbers.add(Integer.parseInt(matcher.group(2)));
                    }
                }
            }
            equations.add(numbers);
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        } catch (NumberFormatException e) {
            System.out.println("The string is not a number!");
        }
    }

    public long countTokens() {
        return countTokens(0);
    }

    /**
     * <p>a * 94 + b * 22 = 8400; a * 34 + b * 67 = 5400;</p>
     * <p><strong>a = (8400 - b * 22) / 94;</strong></p>
     * <p>(8400 - b * 22) / 94 * 34 + b * 67 = 5400;</p>
     * <p>(8400 * 34 - b * 22 * 34 + b * 67 * 94) / 94 = 5400;</p>
     * <p>8400 * 34 - b * (22 * 34 - 67 * 94) = 5400 * 94;</p>
     * <p>8400 * 34 - 5400 * 94 = b * (22 * 34 - 67 * 94);</p>
     * <p><strong>b = (8400 * 34 - 5400 * 94) / (22 * 34 - 67 * 94);</strong></p>
     */
    public long countTokens(long add) {
        return equations.stream().mapToLong(eq -> {
            long b = ((eq.get(4) + add) * eq.get(1) - (eq.get(5) + add) * eq.get(0)) / ((long) eq.get(2) * eq.get(1) - (long) eq.get(3) * eq.get(0));
            long a = (eq.get(4) + add - b * eq.get(2)) / eq.get(0);
            return a > 0 && b > 0 && a * eq.get(0) + b * eq.get(2) == eq.get(4) + add && a * eq.get(1) + b * eq.get(3) == eq.get(5) + add ? a * 3 + b : 0;
        }).sum();
    }

    public long countTokensBinarySearch() {
        return countTokensBinarySearch(0);
    }

    public long countTokensBinarySearch(long add) {
        long result = 0;
        for (List<Integer> eq : new ArrayList<>(equations)) {
            long low = 0;
            long high = add == 0 ? 100 : add;
            boolean reverse = (eq.get(4) + add) / eq.get(2) * eq.get(3) - (eq.get(5) + add) > 0;
            while (low <= high) {
                long a = low + (high - low) / 2;
                long b = eq.get(4) + add - eq.get(0) * a;
                int check = new BigDecimal(b).divide(BigDecimal.valueOf(eq.get(2)), 5, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(eq.get(3))).add(BigDecimal.valueOf(a * eq.get(1) - eq.get(5) - add)).compareTo(BigDecimal.ZERO);
                if (!reverse && check < 0 || reverse && check > 0) {
                    low = a + 1;
                } else if (!reverse && check > 0 || reverse && check < 0) {
                    high = a - 1;
                } else {
                    result += a * 3 + b / eq.get(2);
                    break;
                }
            }
        }
        return result;
    }
}
