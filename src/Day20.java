import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Day20 {
    private final String filePath;
    private final List<char[]> map = new ArrayList<>();
    private Tile start;
    private Tile end;
    private final Map<Tile, Integer> path = new HashMap<>();
    private final int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public Day20(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                char[] characters = line.toCharArray();
                for (int j = 0; j < characters.length; j++) {
                    if (characters[j] == 'S') {
                        start = new Tile(i, j);
                    }
                    if (characters[j] == 'E') {
                        end = new Tile(i, j);
                    }
                }
                map.add(characters);
                i++;
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

    public long countCheats(int minSave, int maxDuration) {
        getPath();
        int result = 0;
        for (Tile cheatStart : path.keySet()) {
            int iMin = Math.max(-maxDuration, -cheatStart.row);
            int iMax = Math.min(maxDuration, map.size() - 1 - cheatStart.row);
            int jMin = Math.max(-maxDuration, -cheatStart.col);
            int jMax = Math.min(maxDuration, map.getFirst().length - 1 - cheatStart.col);
            for (int i = iMin; i <= iMax; i++) {
                for (int j = jMin; j <= jMax; j++) {
                    int duration = Math.abs(i) + Math.abs(j);
                    if (duration <= maxDuration) {
                        Tile cheatEnd = new Tile(cheatStart.row + i, cheatStart.col + j, cheatStart.count + duration);
                        if (path.containsKey(cheatEnd)) {
                            if (path.get(cheatEnd) - cheatEnd.count >= minSave) {
                                result++;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private void getPath() {
        if (start == null) {
            System.out.println("The start position not found!");
            return;
        }
        if (end == null) {
            System.out.println("The end position not found!");
            return;
        }
        if (path.isEmpty()) {
            Tile current = start;
            path.put(current, current.count);
            while (!current.equals(end)) {
                for (int[] direction : directions) {
                    Tile next = new Tile(current.row + direction[0], current.col + direction[1], current.count + 1);
                    if (map.get(next.row)[next.col] != '#' && !path.containsKey(next)) {
                        current = next;
                        path.put(current, current.count);
                    }
                }
            }
        }
    }

    private static class Tile {
        int row;
        int col;
        int count;

        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
            this.count = 0;
        }

        public Tile(int row, int col, int count) {
            this.row = row;
            this.col = col;
            this.count = count;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tile tile = (Tile) o;
            return row == tile.row && col == tile.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }
}
