package com.example.frontend;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class SimpleParser {
    public void parseFile(String filePath) {
        try(FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Scanner scanner = new Scanner(bufferedReader)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
