package ch.fhnw.dist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FileImport {

    public ArrayList<String> readFileContentToList(String file, Boolean SelectDistinct) throws IOException {
        ArrayList<String> lst = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String[] wordSplitted = strLine.split(" ");
                Arrays.asList(wordSplitted).stream().filter(s -> !s.isEmpty()).forEach(s -> lst.add(s));
            }
        }
        if (SelectDistinct) {
            return (ArrayList<String>) lst.stream().distinct().collect(Collectors.toList());
        }
        return lst;
    }
}
