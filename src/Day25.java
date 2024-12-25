import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day25 {
    private final String filePath;
    private final List<int[]> locks = new ArrayList<>();
    private final List<int[]> keys = new ArrayList<>();

    public Day25(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            int[] scheme = new int[5];
            boolean isKey = true;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    Arrays.fill(scheme, 0);
                    row = 0;
                    continue;
                }
                char[] characters = line.toCharArray();
                if (row++ == 0) {
                    isKey = characters[0] != '#';
                    continue;
                }
                for (int i = 0; i < characters.length; i++) {
                    if (characters[i] == '#') {
                        scheme[i]++;
                    }
                }
                if (row > 5) {
                    if (isKey) {
                        keys.add(scheme.clone());
                    } else {
                        locks.add(scheme.clone());
                    }
                    reader.readLine();
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        }
    }

    public int countPairs() {
        int result = 0;
        for (int[] lock : locks) {
            for (int[] key : keys) {
                boolean isFit = true;
                for (int i = 0; i < key.length; i++) {
                    if (lock[i] + key[i] > 5) {
                        isFit = false;
                        break;
                    }
                }
                if (isFit)  {
                    result++;
                }
            }
        }
        return result;
    }
}
