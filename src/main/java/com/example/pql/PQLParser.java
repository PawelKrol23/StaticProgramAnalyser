package com.example.pql;

import java.util.Scanner;

public class PQLParser {
    public void parsePQLs() {
        try(Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String variables = scanner.nextLine();
                String pql = scanner.nextLine();

                // Przykładowy wynik
                System.out.println("2,4");
            }
        }
    }
}
