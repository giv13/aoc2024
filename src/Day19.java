import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 {
    private final String filePath;
    private final List<String> patterns = new ArrayList<>();
    private final List<String> designs = new ArrayList<>();
    private final Map<String, Long> cache = new HashMap<>();

    public Day19(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean isDesigns = false;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    isDesigns = true;
                    continue;
                }
                if (isDesigns) {
                    designs.add(line);
                } else {
                    patterns.addAll(Arrays.asList(line.split(", ")));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        }
    }

    public long getPossibleDesigns() {
        String regex = "(?:" + String.join("|", patterns) + ")+";
        return designs.stream().filter(d -> d.matches(regex)).count();
    }

    public long getAllWays() {
        return designs.stream().mapToLong(this::getAllWays).sum();
    }

    private Long getAllWays(String design) {
        if (!cache.containsKey(design)) {
            cache.put(design, patterns.stream().mapToLong(p -> design.startsWith(p) ? (design.length() == p.length() ? 1 : getAllWays(design.substring(p.length()))) : 0).sum());
        }
        return cache.get(design);
    }
}
