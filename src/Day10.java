import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Day10 {
    private final String filePath;
    private final List<List<Integer>> map = new ArrayList<>();
    private final List<int[]> shifts = new ArrayList<>(List.of(
            new int[]{-1, 0},
            new int[]{0, 1},
            new int[]{1, 0},
            new int[]{0, -1}
    ));

    public Day10(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                map.add(Arrays.stream(line.split("")).map(Integer::parseInt).toList());
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

    public int trailheadsSum() {
        return trailheadsSum(false);
    }

    public int trailheadsSum(boolean isRating) {
        int scores = 0;
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.getFirst().size(); j++) {
                scores += trailheadSum(i, j, 0, isRating ? new ArrayList<>() : new HashSet<>()).size();
            }
        }
        return scores;
    }

    public Collection<String> trailheadSum(int row, int col, int count, Collection<String> reachable) {
        if (row >= 0 && row < map.size() && col >= 0 && col < map.getFirst().size()) {
            if (map.get(row).get(col) == count) {
                if (count == 9) {
                    reachable.add(row + ":" + col);
                } else {
                    for (int[] shift : shifts) {
                        trailheadSum(row + shift[0], col + shift[1], count + 1, reachable);
                    }
                }
            }
        }
        return reachable;
    }
}
