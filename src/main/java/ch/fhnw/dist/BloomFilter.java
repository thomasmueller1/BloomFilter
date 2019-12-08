package ch.fhnw.dist;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BloomFilter {

    BitSet hashes;
    int n, k, m;
    List<HashFunction> hashFunctions;

    // p = Fehlerwahrscheinlichkeit
    public BloomFilter(double p) {
        String wordsPath = BloomFilter.class.getClassLoader().getResource("words.txt").getPath();

        List<String> words = getWordsFromFile(wordsPath);

        m = (int) Math.ceil((n * Math.log(p)) / Math.log(1 / Math.pow(2, Math.log(2)))); // Länge des Bitarrays
        k = (int) Math.round((m / n) * Math.log(2)); // Anzahl Hashfunktionen

        hashes = new BitSet((int) m + 1);


        hashFunctions = getHashfunctions(k);
        for (HashFunction hashFunction : hashFunctions) {
            for (String word : words) {
                hashes.set(Math.abs(hashFunction.hashString(word).asInt() % m));
            }
        }


    }

    private List<String> getWordsFromFile(String wordsPath) {
        FileImport fileImport = new FileImport();

        List<String> words = null;
        try {
            words = fileImport.readFileContentToList(wordsPath, true);
            n = words.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    private static List<HashFunction> getHashfunctions(int k) {
        List<HashFunction> hashFunctions = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            hashFunctions.add(Hashing.murmur3_128(i));
        }
        return hashFunctions;
    }


    public boolean contains(String word) {
        for (HashFunction hashFunction : hashFunctions) {
            int value = Math.abs(hashFunction.hashString(word).asInt() % m);
            if (!hashes.get(value)) return false;
        }
        return true;
    }

    public void checkMultipleWords() {
        String notExistingWordsPath = BloomFilter.class.getClassLoader().getResource("notExistingWords.txt").getPath();
        List<String> notExistingWords = getWordsFromFile(notExistingWordsPath);
        int amountOfTrue = 0;
        for (String word : notExistingWords) {
            if (contains(word))
                amountOfTrue++;
        }
        double p = amountOfTrue / (notExistingWords.size() * 0.01);

        System.out.println("Amount of not existing words: " + notExistingWords.size());
        System.out.println("Amount of matches:            " + amountOfTrue);
        System.out.println("Fehlerwahrscheinlichkeit:     " + new DecimalFormat("#.##").format(p) + "%");
    }

    public static void main(String[] args) {
        BloomFilter bloomFilter2 = new BloomFilter(0.05);
        bloomFilter2.checkMultipleWords();

        System.out.println("-------------------------------------");

        BloomFilter bloomFilter3 = new BloomFilter(0.005);
        bloomFilter3.checkMultipleWords();
    }
}
