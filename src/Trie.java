import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    public TrieNode root;
    public Map<String, Map<String, List<String>>> sequences;

    public Trie() {
        root = new TrieNode();
        sequences = new HashMap<>();
    }

    public void insert(StringBuilder sequence) {
        TrieNode node = root;
        for (int i = 0; i < sequence.length(); i++) {
            char c = sequence.charAt(i);

            int index = indexOf(c);
            if (node.arr[index] == null) {
                TrieNode child = new TrieNode();
                node.arr[index] = child;
                node = child;
            } else {
                node = node.arr[index];
            }
        }

        node.isSequence = true;
    }

    public int search(byte[] corpus, String file, PrintStream out) {
        int matchCount = 0;

        for (int i = 0; i < corpus.length; i++) {
            TrieNode node = root; //set pointer node
            if ((char) corpus[i] != 'N') {
                for (int j = i; j < corpus.length; j++) {
                    //capitalize
                    if (corpus[j] >= 97 && corpus[j] <= 122) { //lowercase letters
                        corpus[j] = (byte) (corpus[j] - 32); //set charAt(i) to its uppercase counterpart, 32 chars away
                    }

                    //check if valid
                    if ((char) corpus[j] != 'A' && (char) corpus[j] != 'C' && (char) corpus[j] != 'T'
                            &&  (char) corpus[j] != 'G' ) {
                        break;
                    }

                    //find index of current nucleobase
                    int index = indexOf((char) corpus[j]);

                    //traverse the trie
                    if (node.arr[index] == null) { //if the node of this index is a leaf, reset pointer to root
                        break;
                    } else { //if the node of this index has a child
                        node = node.arr[index]; //update pointer
                        if (node.isSequence) { //if this node is a valid sequence
                            matchCount++;
                            StringBuilder sequence = new StringBuilder();

                            for (int start = i; start <= j; start++) {
                                if ((char) corpus[start] != 'N') {
                                    sequence.append((char) corpus[start]);
                                }
                            }

                            String hex = String.format("%1$08X", i);
                            System.out.println("\t" + i + " : " + hex + "\t" + sequence);
                            out.println("\t" + i + " : " + hex + "\t" + sequence);

                            String s = new String(sequence);

                            if (!sequences.containsKey(s)) {
                                Map<String, List<String>> value = new HashMap<>();
                                sequences.put(s, value);
                            }

                            if (!sequences.get(s).containsKey(file)) {
                                List<String> indexes = new ArrayList<>();
                                sequences.get(s).put(file, indexes);
                            }

                            sequences.get(s).get(file).add(hex);
                            break;
                        }
                    }
                }
            }
        }

        return matchCount;
    }

    private int indexOf(char c) {
        int index;
        if (c == 'A') {
            index = 0;
        } else if (c == 'T') {
            index = 1;
        } else if (c == 'C') {
            index = 2;
        } else if (c == 'G') {
            index = 3;
        } else {
            index = -1;
        }

        return index;
    }
}
