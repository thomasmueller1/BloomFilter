package ch.fhnw.dist;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BloomFilter {
    public static void main(String[] args) {

        System.out.println("Start Test");
        FileImport fi = new FileImport();
        try {
            System.out.println("Insert path to words file");
            Scanner in = new Scanner(System.in);
            String path = in. nextLine();
            ArrayList<String> x = fi.readFileContentToList(path, true);
            for (String s : x) {
                System.out.println("homas is " + s);
            }
        } catch (IOException e) {
            System.out.println("homas");
        }
    }
}
