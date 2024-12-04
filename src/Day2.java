import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

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
        return safeReportsCount(false);
    }

    public int safeReportsCount(boolean isDumperOn) {
        int result = 0;
        for (List<Integer> report : reports) {
            if (isReportSafe(report) || (isDumperOn && IntStream.range(0, report.size()).anyMatch(i -> {
                List<Integer> newReport = new ArrayList<>(report);
                newReport.remove(i);
                return isReportSafe(newReport);
            }))) {
                result++;
            }
        }
        return result;
    }

    public boolean isReportSafe(List<Integer> report) {
        Integer prev = null;
        Boolean isIncrease = null;
        for (Integer number : report) {
            if (prev != null) {
                if (number.equals(prev) || Math.abs(number - prev) > 3) {
                    return false;
                } else if (isIncrease == null) {
                    isIncrease = number > prev;
                } else if ((isIncrease && number < prev) || (!isIncrease && number > prev)) {
                    return false;
                }
            }
            prev = number;
        }
        return true;
    }
}
