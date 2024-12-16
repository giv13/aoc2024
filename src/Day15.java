import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 {
    private final String filePath;
    private final List<Character[]> map = new ArrayList<>();
    private final List<Character> moves = new ArrayList<>();
    private final Integer[] startPos = new Integer[2];
    private final Map<Character, int[]> shifts = new HashMap<>(Map.of(
            '>', new int[]{0, 1},
            'v', new int[]{1, 0},
            '<', new int[]{0, -1},
            '^', new int[]{-1, 0}
    ));

    public Day15(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int i = 0;
            boolean isMap = true;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    isMap = false;
                    continue;
                }
                List<Character> characters = Arrays.stream(line.split("")).map(ch -> ch.charAt(0)).toList();
                if (isMap) {
                    for (int j = 0; j < characters.size(); j++) {
                        if (characters.get(j) == '@') {
                            startPos[0] = i;
                            startPos[1] = j;
                        }
                    }
                    map.add(characters.toArray(Character[]::new));
                    i++;
                } else {
                    moves.addAll(characters);
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

    public int sumBoxesCoordinates() {
        return sumBoxesCoordinates(false);
    }

    public int sumBoxesCoordinates(boolean widthX2) {
        int result = 0;
        if (startPos[0] != null && startPos[1] != null) {
            List<Character[]> map = getMap(widthX2);
            int[] shiftedPos = new int[]{startPos[0], startPos[1] * (widthX2 ? 2 : 1)};
            for (Character move : moves) {
                int[] shift = shifts.get(move);
                if (move(map, move, shiftedPos[0] + shift[0], shiftedPos[1] + shift[1])) {
                    shiftedPos[0] += shift[0];
                    shiftedPos[1] += shift[1];
                }
            }
            for (int i = 0; i < map.size(); i++) {
                for (int j = 0; j < map.get(i).length; j++) {
                    if (map.get(i)[j] == 'O' || map.get(i)[j] == '[') {
                        result += i * 100 + j;
                    }
                }
            }
        } else {
            System.out.println("The current position of the robot not found!");
        }
        return result;
    }

    private List<Character[]> getMap(boolean widthX2) {
        if (widthX2) {
            List<Character[]> map = new ArrayList<>();
            for (Character[] characters : this.map) {
                List<Character> line = new ArrayList<>();
                for (Character character : characters) {
                    switch (character) {
                        case 'O' -> line.addAll(List.of('[', ']'));
                        case '#' -> line.addAll(List.of('#', '#'));
                        case '@' -> line.addAll(List.of('@', '.'));
                        default -> line.addAll(List.of('.', '.'));
                    }
                }
                map.add(line.toArray(new Character[0]));
            }
            return map;
        }
        return this.map.stream().map(Character[]::clone).toList();
    }

    private boolean move(List<Character[]> map, char move, int row, int col) {
        int[] shift = shifts.get(move);
        List<String> cells = move(map, move, row, col, new ArrayList<>());
        if (cells == null) {
            return false;
        }
        List<Integer[]> coordinates = new ArrayList<>(cells.stream().map(c -> Arrays.stream(c.split(":")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new)).toList());
        coordinates.sort((x, y) -> {
            int comp = x[0].compareTo(y[0]);
            if (comp == 0) {
                comp = x[1].compareTo(y[1]);
            }
            return move == '>' || move == 'v' ? -comp : comp;
        });
        for (Integer[] c : coordinates) {
            Character[] thisRow = map.get(c[0]);
            Character[] prevRow = map.get(c[0] - shift[0]);
            thisRow[c[1]] = prevRow[c[1] - shift[1]];
            prevRow[c[1] - shift[1]] = '.';
        }
        return true;
    }

    private List<String> move(List<Character[]> map, char move, int row, int col, List<String> cells) {
        int[] shift = shifts.get(move);
        cells.add(row + ":" + col);
        switch (map.get(row)[col]) {
            case '[', ']' -> {
                if (move == '>' || move == '<') {
                    return move(map, move, row + shift[0], col + shift[1], cells);
                } else {
                    int nextRow = row + shift[0];
                    int nextCol = col + shift[1];
                    if (cells.contains(nextRow + ":" + nextCol) || move(map, move, nextRow, nextCol, cells) != null) {
                        nextCol += map.get(row)[col] == '[' ? 1 : -1;
                        if (cells.contains(nextRow + ":" + nextCol) || move(map, move, nextRow, nextCol, cells) != null) {
                            return cells;
                        }
                    }
                    return null;
                }
            }
            case 'O' -> {
                return move(map, move, row + shift[0], col + shift[1], cells);
            }
            case '#' -> {
                return null;
            }
            default -> {
                return cells;
            }
        }
    }
}
