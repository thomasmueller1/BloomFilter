package ch.fhnw.dist;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.IOException;
import java.util.*;
import java.text.DecimalFormat;

/**
 * BloomFilter by
 * Markus Winter
 * Thierry Hundth
 * Thomas Müller
 */
public class BloomFilter {
    int n;                            // Anzahl Wörter in Wortliste
    int k;                            // Anzahl Hashfunktionen
    int m;                            // Länge des Bitarrays
    double p;                         // Fehlerwahrscheinlichkeit
    BitSet hashes;                    // Bitarray
    List<HashFunction> hashFunctions; // Liste mit Hashfunktionen

    /**
     * BloomFilter constructor
     * @param p Fehlerwahrscheinlichkeit
     */
    public BloomFilter(double p) {
        this.p = p;
        /* Wörter von Wortliste einlesen */
        String wordsPath = BloomFilter.class.getClassLoader().getResource("words.txt").getPath();
        List<String> words = getWordsFromFile(wordsPath);

        /* Länge von Bitarray berechnen */
        m = (int) Math.ceil((n * Math.log(p)) / Math.log(1 / Math.pow(2, Math.log(2)))); //

        /* Anzahl Hashfunktionen berechnen */
        k = (int) Math.round((m / n) * Math.log(2));

        /* Bitarray mit berechneter Länge erstellen */
        hashes = new BitSet((int) m + 1);

        /* Hashfunktionen erstellen */
        hashFunctions = getHashfunctions(k);

        /* Bitarray abfüllen */
        for (HashFunction hashFunction : hashFunctions) {
            for (String word : words) {
                hashes.set(Math.abs(hashFunction.hashString(word).asInt() % m)); // Modulo Länge Bitarray
            }
        }
    }

    /**
     * Gibt eine strukturierte Wortliste zurück vom gegebenen Pfad
     * @param wordsPath Pfad zu Wortliste
     * @return
     */
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

    /**
     * Generiert eine gegebene Anzahl unterschiedliche murmur3_128 Hashfunktionen.
     * Der Seed wird einfach hochgezählt. Somit ist er garantiert einzigartig.
     * @param k Anzahl Hashfunktionen
     * @return
     */
    private static List<HashFunction> getHashfunctions(int k) {
        List<HashFunction> hashFunctions = new ArrayList<>(k);

        for (int i = 0; i < k; i++) {
            hashFunctions.add(Hashing.murmur3_128(i));
        }

        return hashFunctions;
    }

    /**
     * Macht Ausgaben der Parameter des BloomFilters
     */
    private void printParams() {
        System.out.println("Parameter BloomFilter");
        System.out.println("n (Anzahl Wörter in Wortliste):           " + n);
        System.out.println("m (Länge des Bitarrays):                  " + m);
        System.out.println("k (Anzahl Hashfunktionen):                " + k);
        System.out.println("p (Gegebene Fehlerwahrscheinlichkeit):    " + p*100 + "%");
    }

    /**
     * Prüft ob ein Wort in der BloomFilter-Datenstruktur enthalten ist oder nicht.
     * Wenn false -> stimmt zu 100%
     * Wenn true -> stimmt nur mit einer gewissen Wahrscheinlichkeit
     * @param word Zu prüfendes Wort
     * @return
     */
    public boolean contains(String word) {
        for (HashFunction hashFunction : hashFunctions) {
            int value = Math.abs(hashFunction.hashString(word).asInt() % m);
            if (!hashes.get(value)) return false;
        }
        return true;
    }

    /**
     * Prüft mehrere Wörter ob sie in der BloomFilter-Datenstruktur enthalten sind oder nicht.
     * Macht Ausgaben von Parametern der überprüften Wörter.
     * @param wordlist Zu prüfende Wortliste
     */
    public void checkMultipleWords(String wordlist) {
        String notExistingWordsPath = BloomFilter.class.getClassLoader().getResource(wordlist).getPath();
        List<String> notExistingWords = getWordsFromFile(notExistingWordsPath);
        int amountOfTrue = 0;
        for (String word : notExistingWords) {
            if (contains(word))
                amountOfTrue++;
        }

        double pCalc = amountOfTrue / (notExistingWords.size() * 0.01);

        System.out.println("Anzahl nicht enthaltene Wörter:           " + notExistingWords.size());
        System.out.println("Anzahl Treffer in BloomFilter:            " + amountOfTrue);
        System.out.println("Experimentelle Fehlerwahrscheinlichkeit:  " + new DecimalFormat("#.##").format(pCalc) + "%");
    }

    /**
     * Starterklasse
     * @param args
     */
    public static void main(String[] args) {
        BloomFilter bloomFilter = new BloomFilter(0.005);

        bloomFilter.printParams();
        System.out.println("-----------------------------------------------");
        bloomFilter.checkMultipleWords("notExistingWords.txt");
    }
}
