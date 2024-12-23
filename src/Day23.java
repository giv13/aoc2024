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
import java.util.Set;
import java.util.stream.Collectors;

public class Day23 {
    private final String filePath;
    private final Map<String, Set<String>> connections = new HashMap<>();
    private final Set<String> computers = new HashSet<>();

    public Day23(String filePath) {
        this.filePath = filePath;
        readFile();
    }

    private void readFile() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] connection = line.split("-");
                connections.computeIfAbsent(connection[0], p -> new HashSet<>()).add(connection[1]);
                connections.computeIfAbsent(connection[1], p -> new HashSet<>()).add(connection[0]);
                computers.addAll(Set.of(connection));
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

    public int getCliques() {
        List<Set<String>> cliques = getCliques(new HashSet<>(), new HashSet<>(computers), 3);
        return (int) cliques.stream().filter(clique -> clique.stream().anyMatch(comp -> comp.startsWith("t"))).count();
    }

    public String getMaxClique() {
        Set<String> maxClique = getMaxClique(new HashSet<>(), new HashSet<>(computers), new HashSet<>());
        return maxClique.stream().sorted().collect(Collectors.joining(","));
    }

    private List<Set<String>> getCliques(Set<String> clique, Set<String> candidates, int k) {
        List<Set<String>> result = new ArrayList<>();
        if (clique.size() == k) {
            result.add(new HashSet<>(clique));
        }
        for (String candidate : new HashSet<>(candidates)) {
            clique.add(candidate);
            candidates.remove(candidate);
            result.addAll(getCliques(clique,
                    candidates.stream().filter(c -> connections.get(candidate).contains(c)).collect(Collectors.toSet()), k));
            clique.remove(candidate);
        }
        return result;
    }

    private Set<String> getMaxClique(Set<String> clique, Set<String> candidates, Set<String> not) {
        List<Set<String>> result = new ArrayList<>();
        if (candidates.isEmpty() && not.isEmpty()) {
            result.add(new HashSet<>(clique));
        }
        for (String candidate : new HashSet<>(candidates)) {
            clique.add(candidate);
            candidates.remove(candidate);
            result.add(new HashSet<>(getMaxClique(clique,
                    candidates.stream().filter(c -> connections.get(candidate).contains(c)).collect(Collectors.toSet()),
                    not.stream().filter(n -> connections.get(candidate).contains(n)).collect(Collectors.toSet()))));
            not.add(candidate);
            clique.remove(candidate);
        }
        return result.stream().max(Comparator.comparingInt(Set::size)).orElse(new HashSet<>());
    }
}
