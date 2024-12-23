import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.stream.Collectors.groupingBy;

public class Day18 {
    private final String filePath;
    private final List<int[]> bytePositions = new ArrayList<>();
    private int maxRows = 0;
    private int maxCols = 0;
    private final int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public Day18(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                int[] bytePostition = Arrays.stream(line.split(",")).mapToInt(Integer::parseInt).toArray();
                if (bytePostition[0] > maxRows) {
                    maxRows = bytePostition[0];
                }
                if (bytePostition[1] > maxCols) {
                    maxCols = bytePostition[1];
                }
                bytePositions.add(bytePostition);
            }
            maxRows++;
            maxCols++;
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        } catch (NumberFormatException e) {
            System.out.println("The string is not a number!");
        }
    }

    public int getMinSteps(int corruptedTilesCount) {
        return runAStar(getCorruptedMemspace(corruptedTilesCount));
    }

    public String getFirstPreventingByte() {
        int low = 0;
        int high = bytePositions.size() - 1;
        while (low <= high) {
            int corruptedTilesCount = low + (high - low) / 2;
            int minSteps = runAStar(getCorruptedMemspace(corruptedTilesCount));
            if (minSteps == 0) {
                high = corruptedTilesCount - 1;
            } else {
                low = corruptedTilesCount + 1;
            }
        }
        if (low == bytePositions.size()) {
            return "There are no bytes cutting off the exit.";
        } else {
            return bytePositions.get(high)[0] + "," + bytePositions.get(high)[1];
        }
    }

    private List<boolean[]> getCorruptedMemspace(int corruptedTilesCount) {
        Map<Integer, List<int[]>> bytePositionsGrouped = bytePositions.subList(0, corruptedTilesCount).stream().collect(groupingBy(bp -> bp[1]));
        List<boolean[]> memspace = new ArrayList<>();
        for (int i = 0; i < maxRows; i++) {
            boolean[] row = new boolean[maxCols];
            if (bytePositionsGrouped.containsKey(i)) {
                List<int[]> bytePositions = bytePositionsGrouped.get(i);
                for (int[] bytePosition : bytePositions) {
                    row[bytePosition[0]] = true;
                }
            }
            memspace.add(row);
        }
        return memspace;
    }

    private int runAStar(List<boolean[]> memspace) {
        Queue<Tile> queue = new PriorityQueue<>(Comparator.comparingInt(t -> t.score));
        Tile startTile = new Tile(0, 0);
        Tile endTile = new Tile(maxRows - 1, maxCols - 1);
        queue.add(startTile);
        Map<Tile, Tile> visitedTiles = new HashMap<>();
        visitedTiles.put(startTile, startTile);
        while (!queue.isEmpty()) {
            Tile current = queue.poll();
            if (current.equals(endTile)) {
                return current.count;
            }
            for (int[] direction : directions) {
                int nextRow = current.row + direction[0];
                int nextCol = current.col + direction[1];
                if (nextRow >= 0 && nextRow < maxRows && nextCol >= 0 && nextCol < maxCols) {
                    int nextScore = current.score + Math.abs(nextRow - endTile.row) + Math.abs(nextCol - endTile.col);
                    Tile next = new Tile(nextRow, nextCol, nextScore, current);
                    if (!memspace.get(next.row)[next.col] && (!visitedTiles.containsKey(next) || next.score < visitedTiles.get(next).score)) {
                        queue.add(next);
                        visitedTiles.put(next, next);
                    }
                }
            }
        }
        return 0;
    }

    private static class Tile {
        int row;
        int col;
        int score;
        int count;
        Tile parent;

        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
            this.score = 0;
            this.count = 0;
            this.parent = null;
        }

        public Tile(int row, int col, int score, Tile parent) {
            this.row = row;
            this.col = col;
            this.score = score;
            this.count = parent.count + 1;
            this.parent = parent;
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
