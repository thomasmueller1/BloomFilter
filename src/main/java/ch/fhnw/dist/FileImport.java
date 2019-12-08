package ch.fhnw.dist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helperklasse für den Fileimport
 */
public class FileImport {

    public List<String> readFileContentToList(String file, Boolean SelectDistinct) throws IOException {
        ArrayList<String> lst = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String strLine;
            while ((strLine = br.readLine()) != null) {
                Arrays.asList(strLine).stream().filter(s -> !s.isEmpty()).forEach(s -> lst.add(s.trim()));
            }
        }

        if (SelectDistinct) {
            return lst.stream().distinct().collect(Collectors.toList());
        }

        return lst;
    }
}
