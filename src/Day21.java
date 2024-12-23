import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day21 {
    private final String filePath;
    private final Map<Character, int[]> numKeypad = new HashMap<>(Map.ofEntries(
            Map.entry('7', new int[]{0, 0}),
            Map.entry('8', new int[]{0, 1}),
            Map.entry('9', new int[]{0, 2}),
            Map.entry('4', new int[]{1, 0}),
            Map.entry('5', new int[]{1, 1}),
            Map.entry('6', new int[]{1, 2}),
            Map.entry('1', new int[]{2, 0}),
            Map.entry('2', new int[]{2, 1}),
            Map.entry('3', new int[]{2, 2}),
            Map.entry('G', new int[]{3, 0}),
            Map.entry('0', new int[]{3, 1}),
            Map.entry('A', new int[]{3, 2})
    ));
    private final Map<Character, int[]> dirKeypad = new HashMap<>(Map.ofEntries(
            Map.entry('G', new int[]{0, 0}),
            Map.entry('^', new int[]{0, 1}),
            Map.entry('A', new int[]{0, 2}),
            Map.entry('<', new int[]{1, 0}),
            Map.entry('v', new int[]{1, 1}),
            Map.entry('>', new int[]{1, 2})
    ));
    private final List<String> codes = new ArrayList<>();
    private final Map<String, Long> cache = new HashMap<>();

    public Day21(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                codes.add(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        }
    }

    public long getSum(int level) {
        return codes.stream().mapToLong(c -> getMoves(c, level) * Long.parseLong(c.replaceAll("\\D", ""))).sum();
    }

    private long getMoves(String code, int level) {
        return getMoves(code, level, numKeypad);
    }

    private long getMoves(String code, int level, Map<Character, int[]> keypad) {
        if (level == 0) {
            return code.length();
        }
        if (cache.containsKey(level + code)) {
            return cache.get(level + code);
        }
        long result = 0;
        int[] gap = keypad.get('G');
        int[] start = keypad.get('A');
        for (char symbol : code.toCharArray()) {
            int[] end = keypad.get(symbol);
            int ud = end[0] - start[0];
            int lr = end[1] - start[1];
            String udMove = (ud > 0 ? "v" : "^").repeat(Math.abs(ud));
            String lrMove = (lr > 0 ? ">" : "<").repeat(Math.abs(lr));
            boolean udCheck = end[0] == gap[0] && start[1] == gap[1];
            boolean lrCheck = start[0] == gap[0] && end[1] == gap[1];
            result += Math.min(udCheck ? Long.MAX_VALUE : getMoves(udMove + lrMove + "A", level - 1, dirKeypad), lrCheck ? Long.MAX_VALUE : getMoves(lrMove + udMove + "A", level - 1, dirKeypad));
            start = end;
        }
        cache.put(level + code, result);
        return result;
    }
}
