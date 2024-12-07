import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class Day1 {
    private final String filePath;
    private final List<Integer> leftList = new ArrayList<>();
    private final List<Integer> rightList = new ArrayList<>();

    public Day1(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] numbers = line.split("\\s+");
                if (numbers.length == 2) {
                    leftList.add(Integer.valueOf(numbers[0]));
                    rightList.add(Integer.valueOf(numbers[1]));
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

    public int totalDistance() {
        List<Integer> leftListSorted = leftList.stream().sorted().toList();
        List<Integer> rightListSorted = rightList.stream().sorted().toList();
        /*int result = 0;
        for (int i = 0; i < leftListSorted.size(); i++) {
            result += Math.abs(leftListSorted.get(i) - rightListSorted.get(i));
        }
        return result;*/
        return IntStream.range(0, leftListSorted.size()).map(i -> Math.abs(leftListSorted.get(i) - rightListSorted.get(i))).sum();
    }

    public int similarityScore() {
        Map<Integer, Integer> leftListCounted = leftList.stream().collect(groupingBy(t -> t, collectingAndThen(counting(), Long::intValue)));
        Map<Integer, Integer> rightListCounted = rightList.stream().collect(groupingBy(t -> t, collectingAndThen(counting(), Long::intValue)));
        /*int result = 0;
        for (Map.Entry<Integer, Integer> leftListCountedEntry : leftListCounted.entrySet()) {
            result += leftListCountedEntry.getKey() * leftListCountedEntry.getValue() * rightListCounted.getOrDefault(leftListCountedEntry.getKey(), 0);
        }
        return result;*/
        // * rightListCounted.getOrDefault(i, 0)
        return leftListCounted.entrySet().stream().mapToInt(e -> e.getKey() * e.getValue() * rightListCounted.getOrDefault(e.getKey(), 0)).sum();
    }
}
