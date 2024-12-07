import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day6 {
    private final String filePath;
    private final List<List<Boolean>> map = new ArrayList<>();
    private final List<int[]> shifts = new ArrayList<>(List.of(
            new int[]{-1, 0},
            new int[]{0, 1},
            new int[]{1, 0},
            new int[]{0, -1}
    ));
    private Integer startRow = null;
    private Integer startCol = null;
    private Integer startDirection = null;

    public Day6(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int i = 0;
            Pattern pattern = Pattern.compile("[\\^>v<]");
            Matcher matcher;
            while ((line = reader.readLine()) != null) {
                if (startDirection == null) {
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        switch (line.charAt(matcher.start())) {
                            case '^' -> startDirection = 0;
                            case '>' -> startDirection = 1;
                            case 'v' -> startDirection = 2;
                            case '<' -> startDirection = 3;
                        }
                        startRow = i;
                        startCol = matcher.start();
                    }
                    i++;
                }
                map.add(Arrays.stream(line.split("")).map(e -> e.equals("#")).toList());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        }
    }

    public int countPositions() {
        return countPositions(false);
    }

    public int countPositions(boolean countObstructions) {
        final Map<String, Set<Integer>> positions = new HashMap<>();
        final Set<String> obstructions = new HashSet<>();
        if (startDirection != null) {
            int row = startRow;
            int col = startCol;
            int direction = startDirection;
            int[] shift = shifts.get(direction);
            positions.put(row + ":" + col, new HashSet<>(Set.of(direction)));
            while (row > 0 && row < map.size() - 1 && col > 0 && col < map.getFirst().size() - 1) {
                int nextRow = row + shift[0];
                int nextCol = col + shift[1];
                if (map.get(nextRow).get(nextCol)) {
                    direction = (direction + 1) % 4;
                    shift = shifts.get(direction);
                } else {
                    if (countObstructions) {
                        List<Boolean> nextLine = new ArrayList<>(map.get(nextRow));
                        nextLine.set(nextCol, true);
                        map.set(nextRow, nextLine);
                        if (countPositions(false) == -1) {
                            obstructions.add(nextRow + ":" + nextCol);
                        }
                        nextLine = new ArrayList<>(map.get(nextRow));
                        nextLine.set(nextCol, false);
                        map.set(nextRow, nextLine);
                    }
                    row = nextRow;
                    col = nextCol;
                }
                String position = row + ":" + col;
                if (positions.get(position) != null && positions.get(position).contains(direction)) {
                    return -1;
                } else {
                    positions.computeIfAbsent(position, k -> new HashSet<>()).add(direction);
                }
            }
        } else {
            System.out.println("The current position of the guard not found!");
        }
        if (countObstructions) {
            obstructions.remove(startRow + ":" + startCol);
            return obstructions.size();
        } else {
            return positions.size();
        }
    }
}
