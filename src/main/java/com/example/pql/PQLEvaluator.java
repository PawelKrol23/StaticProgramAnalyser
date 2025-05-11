package com.example.pql;

import com.example.pql.models.Modifies;
import com.example.pql.models.Variable;
import com.example.pql.models.Statement;

import java.util.List;

public class PQLEvaluator {

    public void evaluateQuery(Query query) {
        // Wybierz typ warunku (np. "Modifies")
        if (query.condition.getName().equals("Modifies")) {
            // Jeśli warunek jest typu Modifies, użyj metody getModifies z klasy Modifies
            Modifies modifiesCondition = (Modifies) query.condition;

            // Zakładając, że mamy dostęp do jakiejś zmiennej 'Variable'
            // Możesz tutaj stworzyć odpowiednią instancję zmiennej lub przekazać ją do metody
            Variable var = new Variable(modifiesCondition.var2.getName()); // Przykład stworzenia zmiennej z pierwszego parametru

            // Wywołaj metodę getModifies
            List<Statement> result = modifiesCondition.getCondition(var);

            // Zwróć lub przetwórz wynik
            if (!result.isEmpty()) {
//                System.out.println("Statements that match the condition: ");
                System.out.println(String.join(",", result.stream().map(Statement::toString).toList()));
//                for (Statement statement : result) {
//                    System.out.println(statement);
//                }
            } else {
                System.out.println("None");
            }
        } else {
//            System.out.println("Unsupported condition type.");
        }
    }
}
