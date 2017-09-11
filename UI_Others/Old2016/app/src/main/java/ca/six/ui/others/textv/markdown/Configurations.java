package ca.six.ui.others.textv.markdown;

import java.util.regex.Pattern;

public class Configurations {

    public static class Patterns {
        public static final Pattern SINGLE_LINE_CODE = Pattern.compile("((?!``)`(.+?)`(?!```))|(```(.+?)```)");
        public static final Pattern MULTI_LINE_CODE = Pattern.compile("```(.*?)```", Pattern.DOTALL | Pattern.MULTILINE);
        public static final Pattern BOLD = Pattern.compile("[*]{2}(.+?)[*]{2}");
        public static final Pattern ITALICS = Pattern.compile("\\*(.+?)\\*");
        public static final Pattern STRIKE_THROUGH = Pattern.compile("~{2}(.+?)~{2}");
        public static final Pattern MENTION = Pattern.compile("@(\\w*-?\\w)*");
    }

    public static class Identifiers {
        public static final String BOLD = "**";
        public static final String ITALICS = "*";
        public static final String STRIKE_THROUGH = "~~";
        public static final String SINGLE_LINE_CODE = "`";
        public static final String MULTI_LINE_CODE = "```";
        public static final String MENTION = "@";
    }

    public static class Tokens {
        public static final String BOLD = "[BOLD]";
        public static final String ITALICS = "[ITALICS]";
        public static final String STRIKE_THROUGH = "[STRIKE_THROUGH]";
        public static final String SINGLE_LINE_CODE = "[SINGLE_LINE_CODE]";
        public static final String MULTI_LINE_CODE = "[MULTI_LINE_CODE]";
        public static final String MENTION = "[MENTION]";
    }

}
