import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class NameIndexer {

    // List of names to search for (public so other classes can access it)
    public static final Set<String> NAMES = new HashSet<>(Arrays.asList(
        "James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", "Joseph", "Thomas",
        "Christopher", "Daniel", "Paul", "Mark", "Donald", "George", "Kenneth", "Steven", "Edward", "Brian",
        "Ronald", "Anthony", "Kevin", "Jason", "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey",
        "Frank", "Scott", "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis",
        "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"
    ));

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        String filePath = "data.txt"; // Update this path to match with Local path
        int chunkSize = 1000; // Number of lines per chunk
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Map<String, List<Location>>>> futures = new ArrayList<>();
        Aggregator aggregator = new Aggregator();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineOffset = 0;
            StringBuilder chunk = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                chunk.append(line).append("\n");
                if (++lineOffset % chunkSize == 0) {
                    String textChunk = chunk.toString();
                    futures.add(executor.submit(new Matcher(textChunk, lineOffset - chunkSize)));
                    chunk = new StringBuilder();
                }
            }
            // Process the last chunk if any
            if (chunk.length() > 0) {
                futures.add(executor.submit(new Matcher(chunk.toString(), lineOffset - (lineOffset % chunkSize))));
            }
        }

        // Aggregate results
        for (Future<Map<String, List<Location>>> future : futures) {
            aggregator.aggregate(future.get());
        }

        executor.shutdown();
        aggregator.printResults();
    }
}
