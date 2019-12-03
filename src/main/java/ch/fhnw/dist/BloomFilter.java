package ch.fhnw.dist;

import java.io.IOException;
import java.util.ArrayList;

public class BloomFilter {
    public static void main(String[] args) {

        System.out.println("Start Test");
        FileImport fi = new FileImport();
        try{
            ArrayList<String> x = fi.readFileContentToList("C:\\Users\\thier\\OneDrive\\Dokumente\\fhnw\\dist\\Übungen\\Programmieraufgabe2\\words.txt",true);
            for (String s: x) {
                System.out.println("homas is " + s);
            }
        }catch (IOException e){
            System.out.println("homas");
        }
    }
}
