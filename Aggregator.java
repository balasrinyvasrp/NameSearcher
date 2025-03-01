import java.util.*;

public class Aggregator {
    private Map<String, List<Location>> aggregatedResults = new HashMap<>();

    public void aggregate(Map<String, List<Location>> results) {
        for (Map.Entry<String, List<Location>> entry : results.entrySet()) {
            aggregatedResults.merge(entry.getKey(), entry.getValue(), (oldList, newList) -> {
                oldList.addAll(newList);
                return oldList;
            });
        }
    }

    public void printResults() {
        aggregatedResults.forEach((name, locations) -> {
            System.out.println(name + " --> " + locations);
        });
    }
}
