package com.example.frontend.token;

import com.example.frontend.token.exception.TokenMatchingException;
import lombok.AllArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class Tokenizer {
    private String code;

    public void matchToken(String expectedToken) {
        if(!code.startsWith(expectedToken)) {
            throwMatchingException(expectedToken);
        }

        removeFromCode(expectedToken.length());
    }

    /**
     *
     * @return nazwa zmiennej
     */
    public String matchName() {
        final String nameRegex = "^[a-zA-Z][a-zA-Z0-9#]*";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(code);
        if(!matcher.find()) {
            throw new TokenMatchingException("Could not match name regex: " + nameRegex);
        }

        removeFromCode(matcher.end());
        return matcher.group();
    }

    public String matchInteger() {
        final String integerRegex = "^[0-9]+";
        Pattern pattern = Pattern.compile(integerRegex);
        Matcher matcher = pattern.matcher(code);
        if(!matcher.find()) {
            throw new TokenMatchingException("Could not match integer regex: " + integerRegex);
        }

        removeFromCode(matcher.end());
        return matcher.group();
    }

    public String matchOperator() {
        final String operators = "+-*";
        String startCharacter = Character.toString(code.charAt(0));
        if(!operators.contains(startCharacter)) {
            throw new TokenMatchingException("Could not match any of the operators: +, -, *");
        }

        removeFromCode(1);
        return startCharacter;
    }

    public boolean hasNext() {
        return !code.isBlank();
    }

    public boolean isNextToken(String expectedToken) {
        return code.startsWith(expectedToken);
    }

    public boolean isNextInteger() {
        final String integerRegex = "^[0-9]+";
        Pattern pattern = Pattern.compile(integerRegex);
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }

    private void removeFromCode(int numberOfCharacters) {
        // usuwa znaki z początku kodu i puste znaki jeśli później jakieś są
        this.code = code.substring(numberOfCharacters).trim();
    }

    private void throwMatchingException(String expectedToken) {
        String got = this.code.substring(0, expectedToken.length());
        throw new TokenMatchingException("Expected '" + expectedToken +  "' but got '" + got + "'");
    }

}
