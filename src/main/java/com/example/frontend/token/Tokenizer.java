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

    public boolean isNextToken(String expectedToken) {
        return code.startsWith(expectedToken);
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
