package ca.six.ui.others.textv.markdown;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownTokenizer {

    public static final String SEPARATOR = "[SEPARATOR]";
    public static final String SEPARATOR_PATTERN = "\\[SEPARATOR\\]";

    //Tokenizers
    private static String tokenizeSingleLineCode(String msg) {
        Pattern pattern = Configurations.Patterns.SINGLE_LINE_CODE;
        String identifier = Configurations.Identifiers.SINGLE_LINE_CODE;
        String token = Configurations.Tokens.SINGLE_LINE_CODE;
        return tokenizeWith(msg, pattern, identifier, token);
    }

    private static String tokenizeMultiLineCode(String msg) {
        Pattern pattern = Configurations.Patterns.MULTI_LINE_CODE;
        String identifier = Configurations.Identifiers.MULTI_LINE_CODE;
        String token = Configurations.Tokens.MULTI_LINE_CODE;
        return tokenizeWith(msg, pattern, identifier, token);
    }

    private static String tokenizeBold(String msg) {
        Pattern pattern = Configurations.Patterns.BOLD;
        String identifier = Configurations.Identifiers.BOLD;
        String token = Configurations.Tokens.BOLD;
        return tokenizeWith(msg, pattern, identifier, token);
    }

    private static String tokenizeItalics(String msg) {
        Pattern pattern = Configurations.Patterns.ITALICS;
        String identifier = Configurations.Identifiers.ITALICS;
        String token = Configurations.Tokens.ITALICS;
        return tokenizeWith(msg, pattern, identifier, token);
    }

    private static String tokenizeStrikeThrough(String msg) {
        Pattern pattern = Configurations.Patterns.STRIKE_THROUGH;
        String identifier = Configurations.Identifiers.STRIKE_THROUGH;
        String token = Configurations.Tokens.STRIKE_THROUGH;
        return tokenizeWith(msg, pattern, identifier, token);
    }

    private static String tokenizeMention(String msg) {
        Pattern pattern = Configurations.Patterns.MENTION;
        String identifier = Configurations.Identifiers.MENTION;
        String token = Configurations.Tokens.MENTION;
        return tokenizeWith(msg, pattern, identifier, token);
    }

    private static String tokenizeWith(String tokenizedMesage, Pattern pattern, String identifier, String token) {
        Matcher matcher = pattern.matcher(tokenizedMesage);
        while (matcher.find()) {
            String currentSelection = matcher.group();
            String newSelection = currentSelection.replace(identifier, "");
            newSelection = String.format("%s%s%s%s", SEPARATOR, token, newSelection, SEPARATOR);
            tokenizedMesage = tokenizedMesage.replace(currentSelection, newSelection);
        }

        return tokenizedMesage;
    }


    public static String tokenize(String message) {
        String result;
        result = tokenizeSingleLineCode(message);
        result = tokenizeMultiLineCode(result);
        result = tokenizeBold(result);
        result = tokenizeItalics(result);
        result = tokenizeStrikeThrough(result);
        result = tokenizeMention(result);
        return result;
    }

}
