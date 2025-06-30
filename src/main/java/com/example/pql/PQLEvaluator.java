package com.example.pql;

import com.example.pql.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PQLEvaluator {

    public void evaluateQuery(Query query) {
        if (query.isBooleanQuery()) {
            boolean result = evaluateBooleanQuery(query);
            System.out.println(String.valueOf(result).toLowerCase());
            return;
        }

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
                // Filtruj wyniki tylko jeśli query ma wiele warunków
                finalResult = finalResult.stream()
                        .filter(currentResult::contains)
                        .collect(Collectors.toList());

                if (finalResult.isEmpty()) {
                    System.out.println("none");
                    return;
                }
            }
        }

        if (finalResult != null && !finalResult.isEmpty()) {
            // Dla wyników będących nazwami procedur, nie sortuj po liczbach
            if (finalResult.get(0).getName().matches("[a-zA-Z]+")) {
                finalResult.sort((s1, s2) -> s1.getName().compareTo(s2.getName()));
            } else {
                // Dla numerów statementów sortuj numerycznie
                finalResult.sort((s1, s2) ->
                        Integer.compare(
                                Integer.parseInt(s1.getName()),
                                Integer.parseInt(s2.getName())
                        )
                );
            }

            System.out.println(String.join(",",
                    finalResult.stream()
                            .map(Statement::getName)
                            .collect(Collectors.toList())
            ));
        } else {
            System.out.println("none");
        }
    }

    private boolean evaluateBooleanQuery(Query query) {
        for (Condition condition : query.getConditions()) {
            List<Statement> currentResult = evaluateCondition(condition);
            if (currentResult == null || currentResult.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private List<Statement> evaluateCondition(Condition condition) {
        if (condition.getName().equals("Modifies")) {
            Modifies modifiesCondition = (Modifies) condition;
            // Dla Modifies(1,"x") var2 może być już Variable
            PqlObject var2 = modifiesCondition.var2 instanceof Variable ?
                    modifiesCondition.var2 :
                    new Variable(modifiesCondition.var2.getName());
            return modifiesCondition.getCondition(modifiesCondition.var1, var2);
        }
        else if (condition.getName().equals("Calls")) {
            Calls callsCondition = (Calls) condition;
            return callsCondition.getCondition(callsCondition.var1, callsCondition.var2);
        }
        else if (condition.getName().equals("Calls*")) {
            CallsStar callsStarCondition = (CallsStar) condition;
            List<Statement> result = callsStarCondition.getCondition(
                    callsStarCondition.var1,
                    callsStarCondition.var2
            );
            //System.out.println("DEBUG: Calls* result: " + result);
            return result;
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
        else if (condition.getName().equals("Follows*")) {
            FollowsStar followsStarCondition = (FollowsStar) condition;
            return followsStarCondition.getCondition(followsStarCondition.var1, followsStarCondition.var2);
        }
        return null;
    }
}