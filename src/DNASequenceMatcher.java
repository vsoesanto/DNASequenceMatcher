import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DNASequenceMatcher {
    public static final String TARGETS = "targets";
    public static final String HUMAN_GENOME_CORPUS = "sample_input.txt";

    public static void main(String[] args) throws IOException {
        Trie targetSeqs = new Trie();
        List<String> targets = loadTargets(targetSeqs);
        parseCorpus(targetSeqs, targets);
    }

    public static List<String> loadTargets(Trie targetSeqs) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(TARGETS));
        List<String> targets = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            StringBuilder sequence = new StringBuilder(line);
            targetSeqs.insert(sequence);
            targets.add(new String(sequence));
        }
        return targets;
    }

    public static void parseCorpus(Trie targetSeqs, List<String> targets) throws IOException {
        PrintStream out = new PrintStream(new File("output.txt"));
        long begin = System.currentTimeMillis();
        File directory = new File(HUMAN_GENOME_CORPUS);
        List<String> inputs = new ArrayList<>();
        if (directory.isDirectory()) {
            for (File f : directory.listFiles()) {
                if (f.isFile()) {
                    if (!f.getName().equals(".DS_Store")) {
                        inputs.add(f.getName());
                    }
                }
            }
        }

        Collections.sort(inputs);

        int counts = 0;
//        Searches in a directory of files
//        for (String input : inputs) {
//            File f = new File(HUMAN_GENOME_CORPUS + "/" + input);
//            System.out.println(f.getPath());
//            out.println(f.getPath());
//            byte[] fileContents = Files.readAllBytes(f.toPath());
//            counts = counts + targetSeqs.search(fileContents, input);
//        }

        File f = new File(HUMAN_GENOME_CORPUS);
        System.out.println(f.getPath());
        out.println(f.getPath());
        byte[] fileContents = Files.readAllBytes(f.toPath());
        counts = counts + targetSeqs.search(fileContents, HUMAN_GENOME_CORPUS, out);

        System.out.println("The number of matches in this corpus is = " + counts);
        long finish = System.currentTimeMillis();
        System.out.println("Finishes in " + (finish - begin) + " ms");
        System.out.println();
    }
}

