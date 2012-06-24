package de.weltraumschaf.ebnf.parser;

/**
 * Helper methods for the scanner.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class CharacterHelper {

    private CharacterHelper() { }

    /**
     * Checks whether a character is inside a given character range (included).
     *
     * @param character Character to check.
     * @param start     Including range.
     * @param end       Including range.
     * @return
     */
    public static boolean isCharInRange(final char character, final char start, final char end) {
        if (end < start) {
            throw new IllegalArgumentException("End must be greater or equal than start!");
        }

        return start <= character && character <= end;
    }

    /**
     * Checks whether a character is a alpha [a-zA-Z].
     *
     * @param character A single character.
     *
     * @return
     */
    public static boolean isAlpha(final char character) {
        return isCharInRange(character, 'a', 'z') || isCharInRange(character, 'A', 'Z');
    }

    /**
     * Checks whether a character is a number [0-9].
     *
     * @param character A single character.
     *
     * @return
     */
    public static boolean isNum(final char character) {
        return isCharInRange(character, '0', '9');
    }

    /**
     * Checks whether a character is a number or alpha [0-9a-zA-Z].
     *
     * @param character A single character.
     *
     * @return
     */
    public static boolean isAlphaNum(final char character) {
        return isAlpha(character) || isNum(character);
    }

    /**
     * Checks whether a character is a operator.
     *
     * @param character A single character.
     *
     * @return
     */
    public static boolean isOperator(final char character) {
        switch (character) { // NOPMD
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
     * @param character A single character.
     *
     * @return
     */
    public static boolean isWhiteSpace(final char character) {
        return ' ' == character || '\t' == character || '\n' == character || '\r' == character;
    }

    /**
     * Checks whether a character is a quote ["|'].
     *
     * @param character A single character.
     *
     * @return
     */
    public static boolean isQuote(final char character) {
        return '\'' == character || '"' == character;
    }

    /**
     * Tests a given character if it is equal to ona of the passed test characters.
     *
     * @param character     Character to test.
     * @param characters Array of characters to test against.
     *
     * @return
     */
    public static boolean isEquals(final char character, final char[] characters) {
        for (int i = 0; i < characters.length; ++i) {
            if (character == characters[i]) {
                return true;
            }
        }

        return false;
    }

}
