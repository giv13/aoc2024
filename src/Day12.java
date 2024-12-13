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

public class Day12 {
    private final String filePath;
    private final List<List<Character>> map = new ArrayList<>();
    private final List<int[]> shifts = new ArrayList<>(List.of(
            new int[]{0, 1},
            new int[]{1, 0},
            new int[]{0, -1},
            new int[]{-1, 0}
    ));

    public Day12(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                map.add(Arrays.stream(line.split("")).map(p -> p.charAt(0)).toList());
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

    public int getPrice() {
        return getPrice(false);
    }

    public int getPrice(boolean discount) {
        int result = 0;
        Map<Character, List<List<int[]>>> regions = getRegions(getPlots());
        for (List<List<int[]>> region : regions.values()) {
            for (List<int[]> plots : region) {
                result += plots.size() * (discount ? getSides(plots) : getPerimeter(plots));
            }
        }
        return result;
    }

    private Map<Character, List<int[]>> getPlots() {
        Map<Character, List<int[]>> plots = new HashMap<>();
        for (int i = 0; i < map.size(); i++) {
            List<Character> line = map.get(i);
            for (int j = 0; j < line.size(); j++) {
                plots.computeIfAbsent(line.get(j), p -> new ArrayList<>()).add(new int[]{i, j});
            }
        }
        return plots;
    }

    private Map<Character, List<List<int[]>>> getRegions(Map<Character, List<int[]>> plots) {
        Map<Character, List<List<int[]>>> regions = new HashMap<>();
        for (Map.Entry<Character, List<int[]>> plot : plots.entrySet()) {
            List<List<int[]>> region = new ArrayList<>();
            while (!plot.getValue().isEmpty()) {
                region.add(getRegion(plot.getValue().getFirst(), plot.getValue(), new ArrayList<>()));
            }
            regions.put(plot.getKey(), region);
        }
        return regions;
    }

    private List<int[]> getRegion(int[] current, List<int[]> plots, List<int[]> region) {
        if (current[0] >= 0 && current[0] < map.size() && current[1] >= 0 && current[1] < map.getFirst().size()) {
            for (int i = 0; i < plots.size(); i++) {
                int[] plot = plots.get(i);
                if (plot[0] == current[0] && plot[1] == current[1]) {
                    region.add(plots.remove(i));
                    for (int[] shift : shifts) {
                        getRegion(new int[]{plot[0] + shift[0], plot[1] + shift[1]}, plots, region);
                    }
                    break;
                }
            }
        }
        return region;
    }

    private int getPerimeter(List<int[]> plots) {
        int perimeter = 0;
        for (int[] plot : plots) {
            int sides = 4;
            for (int[] plot2 : plots) {
                if (Math.abs(plot[0] - plot2[0]) == 1 && plot[1] == plot2[1] || Math.abs(plot[1] - plot2[1]) == 1 && plot[0] == plot2[0]) {
                    sides--;
                }
            }
            perimeter += sides;
        }
        return perimeter;
    }

    private int getSides(List<int[]> plots) {
        int sides = 0;
        int[][] corners = new int[2][2];
        Set<String> coordinates = new HashSet<>();
        for (int[] plot : plots) {
            if (plot[0] < corners[0][0]) {
                corners[0][0] = plot[0];
            }
            if (plot[0] > corners[1][0]) {
                corners[1][0] = plot[0];
            }
            if (plot[1] < corners[0][1]) {
                corners[0][1] = plot[1];
            }
            if (plot[1] > corners[1][1]) {
                corners[1][1] = plot[1];
            }
            coordinates.add(plot[0] + ":" + plot[1]);
        }
        Set<Integer> prevLine = new HashSet<>();
        Set<Integer> line = new HashSet<>();
        Set<Integer> nextLine;
        boolean cont;
        for (int i = corners[0][0]; i <= corners[1][0]; i++) {
            cont = false;
            for (int j = corners[0][1]; j <= corners[1][1]; j++) {
                if (coordinates.contains(i + ":" + j)) {
                    if (!cont) {
                        line.add(j + 1);
                    }
                    cont = true;
                } else {
                    cont = false;
                }
            }
            cont = false;
            for (int j = corners[1][1]; j >= corners[0][1]; j--) {
                if (coordinates.contains(i + ":" + j)) {
                    if (!cont) {
                        line.add(-(j + 1));
                    }
                    cont = true;
                } else {
                    cont = false;
                }
            }
            nextLine = new HashSet<>(line);
            line.removeAll(prevLine);
            sides += line.size();
            prevLine = new HashSet<>(nextLine);
            line.clear();
        }
        return sides * 2;
    }
}
