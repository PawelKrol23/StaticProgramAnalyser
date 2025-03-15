package com.example;

import com.example.frontend.SimpleParser;
import com.example.pql.PQLParser;

public class Main {
    public static void main(String[] args) {
        // Parsowanie pliku z kodem Simple
        SimpleParser simpleParser = new SimpleParser();
        simpleParser.parseFile(args[0]);

        // Wypisanie ready dla pipetestera
        // żeby wiedział że plik został już sparsowany
        System.out.println("Ready");

        // Wczytywanie z stdin zapytań pql
        // i wypisywanie odpowiedzi
        PQLParser pqlParser = new PQLParser();
        pqlParser.parsePQLs();
    }
}
