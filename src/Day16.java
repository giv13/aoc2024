import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Day16 {
    private final String filePath;
    private final List<char[]> maze = new ArrayList<>();
    private Tile startTile;
    private Tile endTile;
    private final int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

    public Day16(String filePath) {
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
                        startTile = new Tile(i, j);
                    }
                    if (characters[j] == 'E') {
                        endTile = new Tile(i, j);
                    }
                }
                maze.add(characters);
                i++;
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        }
    }

    public int getScore() {
        return runAStar(false);
    }

    public int countTiles() {
        return runAStar(true);
    }

    public int runAStar(boolean countTiles) {
        if (startTile == null) {
            System.out.println("The start tile of the reindeer not found!");
            return 0;
        }
        if (endTile == null) {
            System.out.println("The end tile of the reindeer not found!");
            return 0;
        }
        Queue<Tile> queue = new PriorityQueue<>(Comparator.comparingInt(t -> t.score));
        queue.add(startTile);
        Map<Tile, Tile> visitedTiles = new HashMap<>();
        visitedTiles.put(startTile, startTile);
        Set<Tile> bestTiles = new HashSet<>();
        int bestScore = 0;
        while (!queue.isEmpty()) {
            Tile current = queue.poll();
            if (current.equals(endTile)) {
                if (bestScore == 0) {
                    bestScore = current.score;
                }
                if (countTiles && bestScore == current.score) {
                    bestTiles.addAll(getPath(current));
                }
                continue;
            }
            for (int i = 0; i < directions.length; i++) {
                Tile next = new Tile(current.row + directions[i][0], current.col + directions[i][1], i, current);
                if (maze.get(next.row)[next.col] != '#' && (!visitedTiles.containsKey(next) || next.score < visitedTiles.get(next).score || (countTiles && next.count == visitedTiles.get(next).count))) {
                    queue.add(next);
                    visitedTiles.put(next, next);
                }
            }
        }
        return countTiles ? bestTiles.size() : bestScore;
    }

    private Set<Tile> getPath(Tile current) {
        Set<Tile> path = new HashSet<>();
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        return path;
    }

    private static class Tile {
        int row;
        int col;
        int dir;
        int score;
        int count;
        Tile parent;

        public Tile(int row, int col) {
            this.row = row;
            this.col = col;
            this.dir = 0;
            this.score = 0;
            this.count = 1;
            this.parent = null;
        }

        public Tile(int row, int col, int dir, Tile parent) {
            this.row = row;
            this.col = col;
            this.dir = dir;
            this.score = parent.score + (dir == parent.dir ? 1 : 1001);
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
