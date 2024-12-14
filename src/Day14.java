import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day14 {
    private final String filePath;
    private final List<int[]> robots = new ArrayList<>();

    public Day14(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                robots.add(Arrays.stream(line.split("\\s?[pv]=|,")).filter(n -> !n.isBlank()).mapToInt(Integer::parseInt).toArray());
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

    public int getSafetyFactor(int maxX, int maxY, int time) {
        List<int[]> robots = this.robots.stream().map(int[]::clone).toList();
        int halfX = maxX / 2 - (maxX % 2 == 0 ? 1 : 0);
        int halfY = maxY / 2 - (maxY % 2 == 0 ? 1 : 0);
        int[] quadrants = new int[4];
        for (int[] robot : robots) {
            for (int i = 0; i < time; i++) {
                setNextPosition(robot, maxX, maxY);
            }
            if ((maxX % 2 == 0 || robot[0] != halfX) && (maxY % 2 == 0 || robot[1] != halfY)) {
                int quadrant = 0;
                if (robot[0] > halfX) {
                    quadrant += 1;
                }
                if (robot[1] > halfY) {
                    quadrant += 2;
                }
                quadrants[quadrant]++;
            }
        }
        return Arrays.stream(quadrants).reduce((x, y) -> x * y).getAsInt();
    }

    public String findEasterEgg(int maxX, int maxY) {
        List<int[]> robots = this.robots.stream().map(int[]::clone).toList();
        int maxLength = 31;
        int maxTries = 1000000;
        boolean[][] drawing = new boolean[maxY][maxX];
        int[][] position = new int[2][2];
        int k = 0;
        int isFound = 0;
        while (isFound < 2 && k++ < maxTries) {
            drawing = new boolean[maxY][maxX];
            for (int[] robot : robots) {
                setNextPosition(robot, maxX, maxY);
                drawing[robot[1]][robot[0]] = true;
            }
            for (int i = 0; i < drawing.length; i++) {
                int length = 0;
                for (int j = 0; j < drawing[i].length; j++) {
                    if (drawing[i][j]) {
                        length++;
                    } else {
                        length = 0;
                    }
                    if (length == maxLength) {
                        position[isFound][0] = i;
                        position[isFound][1] = j - (isFound == 0 ? maxLength - 1 : 0);
                        isFound++;
                    }
                }
            }
        }
        if (isFound == 2) {
            StringBuilder line = new StringBuilder();
            for (int i = position[0][0]; i <= position[1][0]; i++) {
                line.append("\n");
                for (int j = position[0][1]; j <= position[1][1]; j++) {
                    line.append(drawing[i][j] ? "█" : " ");
                }
            }
            return k + line.toString();
        }
        return "The Easter egg is not found!";
    }

    static void setNextPosition(int[] robot, int maxX, int maxY) {
        int posX = robot[0] + robot[2];
        int posY = robot[1] + robot[3];
        if (posX < 0) {
            posX = maxX + posX;
        } else if (posX > maxX - 1) {
            posX = posX - maxX;
        }
        if (posY < 0) {
            posY = maxY + posY;
        } else if (posY > maxY - 1) {
            posY = posY - maxY;
        }
        robot[0] = posX;
        robot[1] = posY;
    }
}
