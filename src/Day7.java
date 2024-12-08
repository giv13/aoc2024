import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7 {
    private final String filePath;
    private final List<List<Long>> equations = new ArrayList<>();
    private final char[] operators = {'+', '*', '|'};

    public Day7(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                equations.add(Arrays.stream(line.split(":?\\s+")).map(Long::parseLong).toList());
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

    public long calibrationResult() {
        return calibrationResult(false);
    }

    public long calibrationResult(boolean allowConcat) {
        long result = 0;
        for (List<Long> equation : equations) {
            if (isEquationCanBeMadeTrue(equation, allowConcat)) {
                result += equation.getFirst();
            }
        }
        return result;
    }

    public Boolean isEquationCanBeMadeTrue(List<Long> numbers, boolean allowConcat) {
        return isEquationCanBeMadeTrue(numbers.get(1), numbers, 2, allowConcat);
    }

    public Boolean isEquationCanBeMadeTrue(long result, List<Long> numbers, int index, boolean allowConcat) {
        boolean isFound = false;
        if (result > numbers.getFirst()) {
            return false;
        } else if (numbers.size() == index) {
            isFound = result == numbers.getFirst();
        } else {
            for (int i = 0; i < operators.length - (allowConcat ? 0 : 1) && !isFound; i++) {
                long newResult;
                switch (operators[i]) {
                    case '+' -> newResult = result + numbers.get(index);
                    case '*' -> newResult = result * numbers.get(index);
                    default -> newResult = Long.parseLong(result + Long.toString(numbers.get(index)));
                }
                isFound = isEquationCanBeMadeTrue(newResult, numbers, index + 1, allowConcat);
            }
        }
        return isFound;
    }
}
