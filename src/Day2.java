import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {
    private final String filePath;
    private final List<List<Integer>> reports = new ArrayList<>();

    public Day2(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                reports.add(Arrays.stream(line.split("\\s+")).map(Integer::parseInt).toList());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден!");
        } catch (IOException e) {
            System.out.println("Ошибка ввода/вывода!");
        } catch (NumberFormatException e) {
            System.out.println("Строка не является числом!");
        }
    }

    public int safeReportsCount() {
        return safeReportsCount(0);
    }

    public int safeReportsCount(int errorCount) {
        int result = 0;
        for (List<Integer> report : reports) {
            Integer prev = null;
            Boolean isIncrease = null;
            boolean isSafe = true;
            int errorCountCheck = errorCount;
            for (Integer number : report) {
                if (prev != null) {
                    if (number.equals(prev) || Math.abs(number - prev) > 3) {
                        if (errorCountCheck-- <= 0) {
                            isSafe = false;
                            break;
                        }
                    } else if (isIncrease == null) {
                        isIncrease = number > prev;
                    } else if ((isIncrease && number < prev) || (!isIncrease && number > prev)) {
                        if (errorCountCheck-- <= 0) {
                            isSafe = false;
                            break;
                        }
                    }
                }
                prev = number;
            }
            if (isSafe) result++;
        }
        return result;
    }
}
