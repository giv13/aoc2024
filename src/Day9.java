import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day9 {
    private final String filePath;
    private final List<Integer> files = new ArrayList<>();
    private final List<Integer> freeSpace = new ArrayList<>();

    public Day9(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            List<Integer> diskMap = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                diskMap.addAll(Arrays.stream(line.split("")).map(Integer::parseInt).toList());
            }
            reader.close();
            for (int i = 0; i < diskMap.size(); i++) {
                if (i % 2 == 0) {
                    files.add(diskMap.get(i));
                } else {
                    freeSpace.add(diskMap.get(i));
                }
            }
            if (files.size() > freeSpace.size()) {
                freeSpace.add(0);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        } catch (NumberFormatException e) {
            System.out.println("The string is not a number!");
        }
    }

    public long fileBlocksChecksum() {
        List<Integer> result = new ArrayList<>();
        List<Integer> files = new ArrayList<>(this.files);
        for (int i = 0; i < files.size(); i++) {
            for (int j = 0; j < files.get(i); j++) {
                result.add(i);
            }
            for (int j = 0; j < freeSpace.get(i) && i < files.size() - 1; j++) {
                int last = files.getLast();
                int lastIndex = files.size() - 1;
                result.add(lastIndex);
                if (--last == 0) {
                    files.removeLast();
                } else {
                    files.set(lastIndex, last);
                }
            }
        }
        return IntStream.range(0, result.size()).mapToLong(i -> (long) i * result.get(i)).sum();
    }

    public long filesChecksum() {
        List<Integer> result = new ArrayList<>();
        List<Integer> files = new ArrayList<>(this.files);
        for (int i = 0; i < files.size(); i++) {
            int file = files.get(i);
            for (int j = 0; j < file * (file < 0 ? -1 : 1); j++) {
                result.add(file < 0 ? 0 : i);
            }
            int space = freeSpace.get(i);
            for (int j = files.size() - 1; j > i; j--) {
                file = files.get(j);
                if (file > 0 && file <= space) {
                    files.set(j, -file);
                    space -= file;
                    while (file-- > 0) {
                        result.add(j);
                    }
                }
            }
            while (space-- > 0) {
                result.add(0);
            }
        }
        return IntStream.range(0, result.size()).mapToLong(i -> (long) i * result.get(i)).sum();
    }
}
