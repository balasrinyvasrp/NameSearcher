import java.util.*;
import java.util.concurrent.*;

public class Matcher implements Callable<Map<String, List<Location>>> {
    private String text;
    private int lineOffset;

    public Matcher(String text, int lineOffset) {
        this.text = text;
        this.lineOffset = lineOffset;
    }

    @Override
    public Map<String, List<Location>> call() {
        Map<String, List<Location>> results = new HashMap<>();
        String[] lines = text.split("\n");
        for (int i = 0; i < lines.length; i++) {
            for (String name : NameIndexer.NAMES) { // Access NAMES from NameIndexer
                int charOffset = 0;
                while ((charOffset = lines[i].indexOf(name, charOffset)) != -1) {
                    results.computeIfAbsent(name, k -> new ArrayList<>()).add(new Location(lineOffset + i, charOffset));
                    charOffset += name.length();
                }
            }
        }
        return results;
    }
}
