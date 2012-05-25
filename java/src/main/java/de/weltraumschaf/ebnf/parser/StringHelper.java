/*
 * LICENSE
 *
 * "THE BEER-WARE LICENSE" (Revision 42):
 * "Sven Strittmatter" <weltraumschaf@googlemail.com> wrote this file.
 * As long as you retain this notice you can do whatever you want with
 * this stuff. If we meet some day, and you think this stuff is worth it,
 * you can buy me a beer in return.
 *
 */

package de.weltraumschaf.ebnf.parser;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class StringHelper {

    private StringHelper() { }

    /**
     * Remove leading and trailing quotes from a string.
     *
     * @param str String to unquote.
     * @return     Unquoted string.
     */
    public static String unquoteString(final String str) {
        // @todo move into own class.
        int start = 0;
        int length = str.length();

        if (CharacterHelper.isQuote(str.charAt(start))) {
            start++;
        }

        if (CharacterHelper.isQuote(str.charAt(length - 1))) {
            length--;
        }

        return str.substring(start, length);
    }
}
