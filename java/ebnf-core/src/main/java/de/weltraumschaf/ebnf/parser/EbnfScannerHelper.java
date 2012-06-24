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

import static de.weltraumschaf.ebnf.parser.CharacterHelper.*;
import java.io.IOException;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
final class EbnfScannerHelper {

    /**
     * Special characters allowed in identifiers.
     */
    private static final char[] SPECIAL_CHARS =  {'-', '_'};

    private EbnfScannerHelper() { }

    /**
     * Scans an identifier [a-zA-Z\-_].
     *
     * @return
     */
    public static Token scanIdentifier(final EbnfScanner scanner) throws IOException {
        final Position position = scanner.createPosition();
        final StringBuilder value = new StringBuilder();
        value.append(scanner.getCurrentCharacter());

        while (scanner.hasNextCharacter()) {
            scanner.nextCharacter();

            if (isAlphaNum(scanner.getCurrentCharacter())
                    || isEquals(scanner.getCurrentCharacter(), SPECIAL_CHARS)) {
                value.append(scanner.getCurrentCharacter());
            } else {
                scanner.backupCharacter();
                break;
            }
        }

        return new Token(TokenType.IDENTIFIER, value.toString(), position);
    }

    /**
     * Scans a literal (any character inside single or double quotes.
     *
     * @return
     */
    public static Token scanLiteral(final EbnfScanner scanner) throws IOException {
        final Position position = scanner.createPosition();
        final char start = scanner.getCurrentCharacter();
        final StringBuilder value = new StringBuilder();
        value.append(start);

        while (scanner.hasNextCharacter()) {
            scanner.nextCharacter();
            value.append(scanner.getCurrentCharacter());

            // Ensure that a lieral opened with " is not temrinated by ' and vice versa.
            if (isQuote(scanner.getCurrentCharacter()) && scanner.getCurrentCharacter() == start) {
                break;
            }
        }

        return new Token(TokenType.LITERAL, value.toString(), position);
    }

    /**
     * Scans a comment (any character inside '(*' and '*)'.
     *
     * @return
     */
    public static Token scanComment(final EbnfScanner scanner) throws IOException {
        final Position postition = scanner.createPosition();
        final StringBuilder value = new StringBuilder();
        value.append(scanner.getCurrentCharacter());

        while (scanner.hasNextCharacter()) {
            scanner.nextCharacter();
            value.append(scanner.getCurrentCharacter());

            if ('*' == scanner.getCurrentCharacter() && ')' == scanner.peekCharacter()) {
                scanner.nextCharacter();
                value.append(scanner.getCurrentCharacter());
                break;
            }

            scanner.checkNewline(); // // Comments cann be multiline.
        }

        return new Token(TokenType.COMMENT, value.toString(), postition);
    }

    /**
     * Scans an operator.
     *
     * @return
     */
    public static Token scanOperator(final EbnfScanner scanner) throws SyntaxException, IOException {
        final Position position   = scanner.createPosition();
        final StringBuilder value = new StringBuilder();
        value.append(scanner.getCurrentCharacter());

        final char peek = scanner.peekCharacter();
        TokenType type = null;

        switch (scanner.getCurrentCharacter()) {
            case ':': {
                type = scanColonOperator(peek, scanner, value);
                break;
            }
            case '=': type = TokenType.ASIGN; break;
            case '.': {
                type = scanDotOperator(peek, scanner, value);
                break;
            }
            case ';': type = TokenType.END_OF_RULE; break;
            case '(': type = TokenType.L_PAREN; break;
            case '[': type = TokenType.L_BRACK; break;
            case '{': type = TokenType.L_BRACE; break;
            case ')': type = TokenType.R_PAREN; break;
            case ']': type = TokenType.R_BRACK; break;
            case '}': type = TokenType.R_BRACE; break;
            case '|': type = TokenType.CHOICE; break;
            default: {
                scanner.raiseError(String.format("Unexpected operator character '%s'",
                                                 scanner.getCurrentCharacter()));
            }
        }

        return new Token(type, value.toString(), position);
    }

    private static TokenType scanColonOperator(final char peek, final EbnfScanner scanner,
                                               final StringBuilder value) throws SyntaxException,
                                                                                 IOException {
        TokenType type;

        if ('=' == peek) {
            scanner.nextCharacter();
            value.append(scanner.getCurrentCharacter());
            scanner.nextCharacter();

            if (scanner.getCurrentCharacter() != '=') {
                scanner.raiseError(String.format("Expecting '=' but seen '%s'",
                                                 scanner.getCurrentCharacter()));
            }

            value.append(scanner.getCurrentCharacter());
            type = TokenType.ASIGN;
        } else {
            type = TokenType.ASIGN;
        }

        return type;
    }

    private static TokenType scanDotOperator(final char peek, final EbnfScanner scanner,
                                             final StringBuilder value) throws IOException {
        TokenType type;
        // range or end of rule
        if ('.' == peek) {
            scanner.nextCharacter();
            value.append(scanner.getCurrentCharacter());
            type = TokenType.RANGE;
        } else {
            type = TokenType.END_OF_RULE;
        }
        return type;
    }

}
