import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day4 {
    private final String filePath;
    private final List<char[]> letters = new ArrayList<>();

    public Day4(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                letters.add(line.toUpperCase().toCharArray());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        }
    }

    public int wordCount(String word) {
        return wordCount(word, false);
    }

    public int wordCount(String word, boolean isX) {
        Map<String, Integer> middleCoordinatesCounts = new HashMap<>();
        final List<int[]> shifts = new ArrayList<>(List.of(
                new int[]{1, 0},
                new int[]{1, 1},
                new int[]{0, 1},
                new int[]{-1, 1},
                new int[]{-1, 0},
                new int[]{-1, -1},
                new int[]{0, -1},
                new int[]{1, -1}
        ));
        word = word.toUpperCase();
        char[] wordByLetters = word.toCharArray();
        boolean isPalindrome = new StringBuilder(word).reverse().toString().equals(word);
        if (!isX || wordByLetters.length % 2 != 0) {
            int middleLetter = wordByLetters.length / 2;
            for (int i = 0; i < letters.size(); i++) {
                for (int j = 0; j < letters.get(i).length; j++) {
                    for (int[] shift : shifts) {
                        if (!isX || (shift[0] != 0 && shift[1] != 0)) {
                            boolean isFound = true;
                            for (int l = 0; l < wordByLetters.length; l++) {
                                if (!checkLetter(wordByLetters[l], i + shift[0] * l, j + shift[1] * l)) {
                                    isFound = false;
                                    break;
                                }
                            }
                            if (isFound) {
                                String middleCoordinates = (i + middleLetter * shift[0]) + ":" + (j + middleLetter * shift[1]);
                                middleCoordinatesCounts.compute(middleCoordinates, (k, v) -> v == null ? 1 : v + 1);
                            }
                        }
                    }
                }
            }
        }
        return isX ?
                middleCoordinatesCounts.values().stream().filter(v -> v == (isPalindrome ? 4 : 2)).mapToInt(v -> 1).sum() :
                middleCoordinatesCounts.values().stream().mapToInt(v -> v).sum() / (isPalindrome ? 2 : 1);
    }

    public boolean checkLetter(char letter, int i, int j) {
        try {
            return letters.get(i)[j] == letter;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
