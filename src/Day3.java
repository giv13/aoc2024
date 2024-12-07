import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
    private final String filePath;
    private final List<String> corruptedMemory = new ArrayList<>();

    public Day3(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                corruptedMemory.add(line);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        }
    }

    public int multiplications(String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher;
        boolean isDo = true;
        int result = 0;
        for (String corruptedLine : corruptedMemory) {
            matcher = pattern.matcher(corruptedLine);
            while (matcher.find()) {
                switch (matcher.group(1)) {
                    case "do" -> isDo = true;
                    case "don't" -> isDo = false;
                    case "mul" -> result += isDo ? Integer.parseInt(matcher.group(2)) * Integer.parseInt(matcher.group(3)) : 0;
                }
            }
        }
        return result;
    }
}
