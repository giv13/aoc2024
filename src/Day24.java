import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Day24 {
    private final String filePath;
    private final Map<String, Boolean> wires = new HashMap<>();
    private final List<String[]> gates = new ArrayList<>();

    public Day24(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean isGates = false;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    isGates = true;
                    continue;
                }
                String[] split = line.split("[:>\\-\\s]+");
                if (isGates) {
                    gates.add(split);
                } else {
                    wires.put(split[0], split[1].equals("1"));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println("I/O error!");
        }
    }

    public long simulate() {
        Map<String, Boolean> wires = new HashMap<>(this.wires);
        List<String[]> gates = new ArrayList<>(this.gates);
        while(!gates.isEmpty()) {
            gates.removeIf(g -> {
                if (wires.containsKey(g[0]) && wires.containsKey(g[2])) {
                    wires.put(g[3], getOutput(wires.get(g[0]), wires.get(g[2]), g[1]));
                    return true;
                }
                return false;
            });
        }
        String binary = getBinary(wires);
        return Long.parseLong(binary, 2);
    }

    /**
     * It works with my input, but doesn't work with swapping wkg and bdg for example, maybe I'll find the better solution later
     */
    public String getSwappedWires() {
        Map<String, Set<String>> ops = new HashMap<>();
        List<String> swapped = new ArrayList<>();
        for (String[] gate : gates) {
            ops.computeIfAbsent(gate[0], k -> new HashSet<>()).add(gate[1]);
            ops.computeIfAbsent(gate[2], k -> new HashSet<>()).add(gate[1]);
        }
        for (String[] gate : gates) {
            if ((gate[1].equals("AND") && !(gate[0].endsWith("00") && gate[2].endsWith("00")) && (!ops.containsKey(gate[3]) || ops.get(gate[3]).size() != 1 || !ops.get(gate[3]).contains("OR")))
                    || (gate[1].equals("OR") && (!(gate[3].startsWith("z") && gate[3].endsWith(String.valueOf(wires.size() / 2))) && (!ops.containsKey(gate[3]) || ops.get(gate[3]).size() != 2 || !ops.get(gate[3]).contains("AND") || !ops.get(gate[3]).contains("XOR"))))
                    || (gate[1].equals("XOR") && !gate[3].startsWith("z") && ((!gate[0].startsWith("x") && !gate[2].startsWith("x")) || !ops.containsKey(gate[3]) || ops.get(gate[3]).size() != 2 || !ops.get(gate[3]).contains("AND") || !ops.get(gate[3]).contains("XOR")))) {
                swapped.add(gate[3]);
            }
        }
        return (swapped.isEmpty() ? "The system is working correctly." : swapped.stream().sorted().collect(Collectors.joining(","))) + visualize();
    }

    private boolean getOutput(boolean wire1, boolean wire2, String op) {
        boolean result;
        switch (op) {
            case "AND" -> result = wire1 && wire2;
            case "OR" -> result = wire1 || wire2;
            default -> result = wire1 ^ wire2;
        }
        return result;
    }

    private String getBinary(Map<String, Boolean> wires) {
        return wires.keySet().stream().filter(w -> w.startsWith("z")).sorted(Collections.reverseOrder()).map(w -> wires.get(w) ? "1" : "0").collect(Collectors.joining(""));
    }

    private String visualize() {
        List<String[]> gates = new ArrayList<>(this.gates.stream().sorted(Comparator.comparing(g -> g[1].equals("OR") ? 2 : (g[1].equals("AND") ? 1 : 0))).toList());
        List<String> wiresSorted = wires.keySet().stream().sorted(Comparator.comparing((String w) -> Integer.parseInt(w.replaceAll("\\D", ""))).thenComparing(w -> w)).toList();
        StringBuilder vis = new StringBuilder();
        int length = 0;
        for (String wire : wiresSorted) {
            if (wire.startsWith("x")) {
                length++;
            }
            vis.append("\n").append(visualize(wire, gates, length));
        }
        return vis.toString();
    }

    private String visualize(String wire, List<String[]> gates, int length) {
        StringBuilder vis = new StringBuilder();
        if (length > 1 && (wire.startsWith("x") || wire.startsWith("y"))) {
            vis.append(" ".repeat(23 * ((length - 2) % 8)));
        }
        vis.append(wire);
        String[] gate = null;
        int i;
        for (i = 0; i < gates.size(); i++) {
            if (gates.get(i)[0].equals(wire) || gates.get(i)[2].equals(wire)) {
                gate = gates.get(i);
                break;
            }
        }
        if (gate == null) {
            if (!wire.startsWith("z")) {
                vis.append("═".repeat(13)).append("╝").append(" ".repeat(10)).append("║");
            }
            return vis.toString();
        }
        gates.remove(i);
        vis.append(gate[1].equals("AND") ? "═╩═(" : "═╦═(").append(gate[1]).append(")═").append(visualize(gate[3], gates, length));
        if (wire.startsWith("y") && gate[1].equals("AND") && length > 1 && (length - 1) % 8 == 0) {
            vis.append("\n").append(" ".repeat(16)).append("╔").append("═".repeat(183)).append("╝");
        }
        if (wire.startsWith("y") && gates.isEmpty()) {
            vis.setLength(vis.length() - 9);
        }
        return vis.toString();
    }
}
