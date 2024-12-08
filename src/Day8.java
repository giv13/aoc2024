import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day8 {
    private final String filePath;
    private final Map<Character, List<int[]>> map = new HashMap<>();
    private int rows = 0;
    private int cols = 0;

    public Day8(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                char[] symbols = line.toCharArray();
                if (i == 0) {
                    cols = symbols.length;
                }
                for (int j = 0; j < symbols.length; j++) {
                    if (symbols[j] != '.') {
                        map.computeIfAbsent(symbols[j], k -> new ArrayList<>()).add(new int[]{i, j});
                    }
                }
                i++;
            }
            rows = i;
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        }
    }

    public int countAntinodes() {
        return countAntinodes(false);
    }

    public int countAntinodes(boolean updatedModel) {
        Set<String> antinodes = new HashSet<>();
        for (List<int[]> coords : map.values()) {
            for (int i = 0; i < coords.size(); i++) {
                for (int j = 0; j < coords.size(); j++) {
                    if (i != j) {
                        int[] shift = {coords.get(j)[0] - coords.get(i)[0], coords.get(j)[1] - coords.get(i)[1]};
                        int[] antinode = coords.get(j).clone();
                        if (updatedModel) {
                            antinodes.add(antinode[0] + ":" + antinode[1]);
                        }
                        while (true) {
                            antinode[0] += shift[0];
                            antinode[1] += shift[1];
                            if (antinode[0] >= 0 && antinode[0] < rows && antinode[1] >= 0 && antinode[1] < cols) {
                                antinodes.add(antinode[0] + ":" + antinode[1]);
                            } else {
                                break;
                            }
                            if (!updatedModel) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        return antinodes.size();
    }
}
