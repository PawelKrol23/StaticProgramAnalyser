package com.example.pql;

import com.example.pql.models.*;

import java.util.ArrayList;
import java.util.List;

public class PQLEvaluator {

    public void evaluateQuery(Query query) {
        List<Statement> finalResult = null;

        for (Condition condition : query.getConditions()) {
            List<Statement> currentResult = evaluateCondition(condition);

            if (currentResult == null || currentResult.isEmpty()) {
                System.out.println("none");
                return;
            }

            if (finalResult == null) {
                finalResult = new ArrayList<>(currentResult);
            } else {
                // Znajdujemy część wspólną wyników
                finalResult.retainAll(currentResult);
            }

            if (finalResult.isEmpty()) {
                System.out.println("none");
                return;
            }
        }

        if (finalResult != null && !finalResult.isEmpty()) {
            // Sortujemy wyniki przed wyświetleniem
            finalResult.sort((s1, s2) ->
                    Integer.compare(
                            Integer.parseInt(s1.getName()),
                            Integer.parseInt(s2.getName())
                    )
            );
            System.out.println(String.join(",", finalResult.stream()
                    .map(Statement::toString)
                    .toList()));
        } else {
            System.out.println("none");
        }
    }

    private List<Statement> evaluateCondition(Condition condition) {
        if (condition.getName().equals("Modifies")) {
            Modifies modifiesCondition = (Modifies) condition;
            Variable var2 = new Variable(modifiesCondition.var2.getName());
            return modifiesCondition.getCondition(modifiesCondition.var1, var2);
        }
        else if (condition.getName().equals("Calls")) {
            Calls callsCondition = (Calls) condition;
            Variable var2 = new Variable(callsCondition.var2.getName());
            return callsCondition.getCondition(callsCondition.var1, callsCondition.var2);
        }
        else if (condition.getName().equals("Follows")) {
            Follows followsCondition = (Follows) condition;
            Variable var2 = new Variable(followsCondition.var2.getName());
            return followsCondition.getCondition(followsCondition.var1, followsCondition.var2);
        }
        else if (condition.getName().equals("Uses")) {
            Uses usesCondition = (Uses) condition;
            Variable var2 = new Variable(usesCondition.var2.getName());
            return usesCondition.getCondition(usesCondition.var1, var2);
        }
        else if (condition.getName().equals("Parent")) {
            Parent parentCondition = (Parent) condition;
            return parentCondition.getCondition(parentCondition.var1, parentCondition.var2);
        }

        return null;
    }
}