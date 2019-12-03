package ch.fhnw.dist;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Scanner;

public class BloomFilter {

    BitSet hashes;
    int n, k, m;

    // p = Fehlerwahrscheinlichkeit
    public BloomFilter(double p) {
        FileImport fileImport = new FileImport();
        Scanner in = new Scanner(System.in);
        String path = in.nextLine();
        List<String> words = null;
        try {
            words = fileImport.readFileContentToList(path, true);
            n = words.size();
        } catch (IOException e) {
            e.printStackTrace();
        }

        m = (int) Math.ceil((n * Math.log(p)) / Math.log(1 / Math.pow(2, Math.log(2)))); // Länge des Bitarrays
        k = (int) Math.round((m / n) * Math.log(2)); // Anzahl Hashfunktionen

        hashes = new BitSet((int) m + 1);


        List<HashFunction> hashFunctions = getHashfunctions(k);
        for (HashFunction hashFunction : hashFunctions) {
            for (String word : words) {
                hashes.set(Math.abs(hashFunction.hashString(word).asInt() % m));
            }
        }


    }

    private List<HashFunction> getHashfunctions(int k) {
        List<HashFunction> hashFunctions = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            hashFunctions.add(Hashing.murmur3_128(i));
        }
        return hashFunctions;
    }


    public boolean contains(String word) {
        List<HashFunction> hashFunctions = getHashfunctions(k);
        for (HashFunction hashFunction : hashFunctions) {
            int value = Math.abs(hashFunction.hashString(word).asInt() % m);
            if (!hashes.get(value)) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        BloomFilter bloomFilter = new BloomFilter(0.1);
        System.out.println(bloomFilter.contains("Thierry"));
        System.out.println(bloomFilter.contains("het"));
        System.out.println(bloomFilter.contains("e"));
        System.out.println(bloomFilter.contains("fetti"));
        System.out.println(bloomFilter.contains("wormy"));
        System.out.println(bloomFilter.contains("Mueter"));
    }
}
