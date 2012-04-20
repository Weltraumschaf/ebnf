package de.weltraumschaf.ebnf;

/**
 * Helper methods for the scanner.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ScannerHelper {

    /**
     * Checks whether a character is a alpha [a-zA-Z].
     *
     * @param c A single character.
     *
     * @return
     */
    public static boolean isAlpha(char c) {
        return c > 64 && c < 91 || c > 96 && c < 123;
    }

    /**
     * Checks whether a character is a number [0-9].
     *
     * @param c A single character.
     *
     * @return
     */
    public static boolean isNum(char c) {
        return c > 47 && c < 58;
    }

    /**
     * Checks whether a character is a number or alpha [0-9a-zA-Z].
     *
     * @param c A single character.
     *
     * @return
     */
    public static boolean isAlphaNum(char c) {
        return isAlpha(c) || isNum(c);
    }

    /**
     * Checks whether a character is a operator.
     *
     * @param c A single character.
     *
     * @return
     */
    public static boolean isOperator(char c) {
        switch (c) {
            case '{':
            case '}':
            case '(':
            case ')':
            case '[':
            case ']':
            case ',':
            case ';':
            case '.':
            case ':':
            case '|':
            case '=':
            case '-':
                return true;
            default:
                return false;
        }
    }

    /**
     * Checks whether a character is a whitespace.
     *
     * @param c A single character.
     *
     * @return
     */
    public static boolean isWhiteSpace(char c) {
        return ' ' == c || '\t' == c || '\n' == c || '\r' == c;
    }

    /**
     * Checks whether a character is a quote ["|'].
     *
     * @param c A single character.
     *
     * @return
     */
    public static boolean isQuote(char c) {
        return '\'' == c || '"' == c;
    }

    /**
     * Tests a given character if it is equal to ona of the passed test characters.
     *
     * @param c     Character to test.
     * @param chars Array of characters to test against.
     *
     * @return
     */
    public static boolean isEquals(char c, char[] chars) {
        for (int i = 0; i < chars.length; ++i) {
            if (c == chars[i]) {
                return true;
            }
        }

        return false;
    }

}
