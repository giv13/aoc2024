import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day5 {
    private final String filePath;
    private final Map<Integer, Set<Integer>> orderingRules = new HashMap<>();
    private final List<List<Integer>> updates = new ArrayList<>();

    public Day5(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.matches("^\\d+\\|\\d+$")) {
                    String[] rule = line.split("\\|");
                    Integer before = Integer.valueOf(rule[0]);
                    Integer after = Integer.valueOf(rule[1]);
                    orderingRules.computeIfAbsent(before, k -> new HashSet<>()).add(after);
                } else if (line.matches("^[\\d,]+$")) {
                    List<Integer> update = Arrays.stream(line.split(",")).map(Integer::parseInt).toList();
                    updates.add(update);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        }
    }

    public int sumMiddleNumbers() {
        return sumMiddleNumbers(false);
    }

    public int sumMiddleNumbers(boolean incorrectMode) {
        int result = 0;
        for (List<Integer> update : updates) {
            if (update.size() % 2 == 1) {
                if (!incorrectMode && checkUpdate(update)) {
                    result += update.get(update.size() / 2);
                } else if (incorrectMode && !checkUpdate(update)) {
                    List<Integer> orderedUpdate = orderUpdate(update);
                    if (orderedUpdate != null) {
                        result += orderedUpdate.get(update.size() / 2);
                    }
                }
            }
        }
        return result;
    }

    public boolean checkUpdate(List<Integer> update) {
        for (int i = 0; i < update.size() - 1; i++) {
            Set<Integer> rules = orderingRules.get(update.get(i));
            if (rules == null || !rules.containsAll(update.subList(i + 1, update.size()))) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> orderUpdate(List<Integer> update) {
        List<Integer> upd = new ArrayList<>(update);
        List<Integer> result = new ArrayList<>();
        boolean isSorted = true;
        while (!upd.isEmpty() && isSorted) {
            if (upd.size() == 1) {
                result.add(upd.getLast());
                upd.removeLast();
            } else {
                for (int i = 0; i < upd.size(); i++) {
                    int page = upd.get(i);
                    Set<Integer> rules = orderingRules.get(page);
                    if (rules != null && rules.containsAll(upd.stream().filter(n -> !n.equals(page)).toList())) {
                        result.add(page);
                        upd.remove(i);
                        break;
                    }
                    if (i == upd.size() - 1) {
                        isSorted = false;
                    }
                }
            }

        }
        return isSorted ? result : null;
    }
}
