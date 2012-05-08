package de.weltraumschaf.ebnf.util;

/**
 * Helper methods for the scanner.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ScannerHelper {

    /**
     * Checks whether a character is inside a given character range (included).
     *
     * @param c     Character to check.
     * @param start Including range.
     * @param end   Including range.
     * @return
     */
    public static boolean isCharInRange(char c, char start, char end) {
        if (end < start) {
            throw new IllegalArgumentException("End must be greater or equal than start!");
        }

        return start <= c && c <= end;
    }

    /**
     * Checks whether a character is a alpha [a-zA-Z].
     *
     * @param c A single character.
     *
     * @return
     */
    public static boolean isAlpha(char c) {
        return isCharInRange(c, 'a', 'z') || isCharInRange(c, 'A', 'Z');
    }

    /**
     * Checks whether a character is a number [0-9].
     *
     * @param c A single character.
     *
     * @return
     */
    public static boolean isNum(char c) {
        return isCharInRange(c, '0', '9');
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
