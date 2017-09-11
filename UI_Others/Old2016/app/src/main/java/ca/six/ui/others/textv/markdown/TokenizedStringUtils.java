package ca.six.ui.others.textv.markdown;

/**
 * Created by asantibanez on 10/1/16.
 */

public class TokenizedStringUtils {

    public static boolean isBold(String tokenizedMessage) {
        return tokenizedMessage.contains(Configurations.Tokens.BOLD);
    }

    public static boolean isItalics(String tokenizedMessage) {
        return tokenizedMessage.contains(Configurations.Tokens.ITALICS);
    }

    public static boolean isStrikeThrough(String tokenizedMessage) {
        return tokenizedMessage.contains(Configurations.Tokens.STRIKE_THROUGH);
    }

    public static boolean isSingleLineCode(String tokenizedMessage) {
        return tokenizedMessage.contains(Configurations.Tokens.SINGLE_LINE_CODE);
    }

    public static boolean isMultiLineCode(String tokenizedMessage) {
        return tokenizedMessage.contains(Configurations.Tokens.MULTI_LINE_CODE);
    }

    public static boolean isMention(String tokenizedMessage) {
        return tokenizedMessage.contains(Configurations.Tokens.MENTION);
    }

}
