package de.weltraumschaf.ebnf;

import de.weltraumschaf.ebnf.util.ReaderHelper;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

/**
 * Unit test for Scanner.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ScannerTest {

    class Expectation {

        private final String value;
        private final TokenType type;
        private final int line;
        private final int col;

        public Expectation(String value, TokenType type, int line, int col) {
            this.value = value;
            this.type = type;
            this.line = line;
            this.col = col;
        }

        public int getCol() {
            return col;
        }

        public int getLine() {
            return line;
        }

        public TokenType getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }
    
    private static final String FIXTURE_DIR = "/de/weltraumschaf/ebnf";

    private BufferedReader createSourceFromFixture(String fixtureFile) throws FileNotFoundException,
                                                                              URISyntaxException {
        URL resource = getClass().getResource(FIXTURE_DIR + '/' + fixtureFile);
        return ReaderHelper.createFrom(resource.toURI());
    }

    private void assertTokens(BufferedReader grammar, List<Expectation> expectations, String msg)
            throws SyntaxError, IOException {
        Scanner scanner = new Scanner(grammar);
        int count = 0;

        while (scanner.hasNextToken()) {
            scanner.nextToken();
            Token token = scanner.currentToken();
            assertNotNull(token);
            Expectation expectation = expectations.get(count);
            assertEquals(String.format("%s %d type: %s", msg, count, token.getValue()), expectation.
                    getType(), token.getType());
            assertEquals(String.format("%s %d value: %s", msg, count, token.getValue()),
                         expectation.getValue(), token.getValue());

            Position position = token.getPosition();
            assertNull(position.getFile());
            assertEquals(String.format("%s %d line: %s", msg, count, token.getValue()), expectation.
                    getLine(), position.getLine());
            assertEquals(String.format("%s %d col: %s", msg, count, token.getValue()), expectation.
                    getCol(), position.getColumn());
            ++count;
        }

        assertEquals("Not enough tokens!", expectations.size(), count);

        int[] backtracks = {1, 3, 20, 200000};
        for (int i = 0; i < backtracks.length; ++i) {
            int backtrack = backtracks[i];
            int index = count - (backtrack + 1);

            if (index < 0) {
                try {
                    scanner.backtrackToken(backtrack);
                    fail("Expected excpetion not thrown!");
                } catch (IllegalArgumentException ex) {
                    // Exception is expected here.
                }
            } else {
                Token token = scanner.backtrackToken(backtrack);
                Expectation expectation = expectations.get(index);
                assertEquals(expectation.getType(), token.getType());
                assertEquals(expectation.getValue(), token.getValue());
                Position position = token.getPosition();
                assertNull(position.getFile());
                assertEquals(expectation.getLine(), position.getLine());
                assertEquals(expectation.getCol(), position.getColumn());
            }
        }

        scanner.close();
    }

    @Test public void testNext() throws Exception {
        Scanner scanner = new Scanner(ReaderHelper.createFrom(""));
        assertNull(scanner.currentToken());
        assertNull(scanner.currentToken());

        assertTokens(createSourceFromFixture("rules_with_ranges.ebnf"), Arrays.asList(
                new Expectation("\"Rules with ranges.\"", TokenType.LITERAL, 1, 1),
                new Expectation("{", TokenType.L_BRACE, 1, 22),
                new Expectation("lower", TokenType.IDENTIFIER, 2, 5),
                new Expectation("=", TokenType.ASIGN, 2, 15),
                new Expectation("\"a\"", TokenType.LITERAL, 2, 17),
                new Expectation("..", TokenType.RANGE, 2, 21),
                new Expectation("\"z\"", TokenType.LITERAL, 2, 24),
                new Expectation(".", TokenType.END_OF_RULE, 2, 28),
                new Expectation("upper", TokenType.IDENTIFIER, 3, 5),
                new Expectation("=", TokenType.ASIGN, 3, 15),
                new Expectation("\"A\"", TokenType.LITERAL, 3, 17),
                new Expectation("..", TokenType.RANGE, 3, 21),
                new Expectation("\"Z\"", TokenType.LITERAL, 3, 24),
                new Expectation(".", TokenType.END_OF_RULE, 3, 28),
                new Expectation("number", TokenType.IDENTIFIER, 4, 5),
                new Expectation("=", TokenType.ASIGN, 4, 15),
                new Expectation("\"0\"", TokenType.LITERAL, 4, 17),
                new Expectation("..", TokenType.RANGE, 4, 21),
                new Expectation("\"9\"", TokenType.LITERAL, 4, 24),
                new Expectation(".", TokenType.END_OF_RULE, 4, 28),
                new Expectation("alpha-num", TokenType.IDENTIFIER, 5, 5),
                new Expectation("=", TokenType.ASIGN, 5, 15),
                new Expectation("\"a\"", TokenType.LITERAL, 5, 17),
                new Expectation("..", TokenType.RANGE, 5, 21),
                new Expectation("\"z\"", TokenType.LITERAL, 5, 24),
                new Expectation("|", TokenType.CHOICE, 5, 28),
                new Expectation("\"0\"", TokenType.LITERAL, 5, 30),
                new Expectation("..", TokenType.RANGE, 5, 34),
                new Expectation("\"9\"", TokenType.LITERAL, 5, 37),
                new Expectation(".", TokenType.END_OF_RULE, 5, 41),
                new Expectation("}", TokenType.R_BRACE, 6, 1),
                new Expectation(null, TokenType.EOF, 6, 1)), "Rules with range.");

        assertTokens(createSourceFromFixture("rules_with_comments.ebnf"), Arrays.asList(
            new Expectation("\"Rules with comments.\"", TokenType.LITERAL,    1, 1),
            new Expectation("{",       TokenType.L_BRACE,   1, 24),

            new Expectation("title",   TokenType.IDENTIFIER, 2, 5),
            new Expectation("=",       TokenType.ASIGN,   2, 16),
            new Expectation("literal", TokenType.IDENTIFIER, 2, 18),
            new Expectation(".",       TokenType.END_OF_RULE,   2, 26),
            new Expectation("(* Comment * at the end of line *)",
                                        TokenType.COMMENT,    2, 28),

            new Expectation("comment", TokenType.IDENTIFIER, 3, 5),
            new Expectation("=",       TokenType.ASIGN,   3, 16),
            new Expectation("literal", TokenType.IDENTIFIER, 3, 18),
            new Expectation(".",       TokenType.END_OF_RULE,   3, 26),

            new Expectation("(*  This is a multi\n        line comment. *)", TokenType.COMMENT,    4, 5),

            new Expectation("comment", TokenType.IDENTIFIER, 6, 5),
            new Expectation("=",       TokenType.ASIGN,   6, 16),
            new Expectation("literal", TokenType.IDENTIFIER, 6, 18),
            new Expectation(".",       TokenType.END_OF_RULE,   6, 26),

            new Expectation("}",       TokenType.R_BRACE,   7, 1),
            new Expectation(null,        TokenType.EOF,        7, 1)
        ), "Rule with comment.");

        assertTokens(createSourceFromFixture("rules_with_different_assignment_ops.ebnf"), Arrays.asList(
            new Expectation("\"Rules with different assignment operators.\"",
                                        TokenType.LITERAL,    1, 1),
            new Expectation("{",       TokenType.L_BRACE,   1, 46),

            new Expectation("comment1",TokenType.IDENTIFIER, 2, 5),
            new Expectation("=",       TokenType.ASIGN,   2, 14),
            new Expectation("literal1",TokenType.IDENTIFIER, 2, 18),
            new Expectation(".",       TokenType.END_OF_RULE,   2, 27),

            new Expectation("comment2",TokenType.IDENTIFIER, 3, 5),
            new Expectation(":",       TokenType.ASIGN,   3, 14),
            new Expectation("literal2",TokenType.IDENTIFIER, 3, 18),
            new Expectation(".",       TokenType.END_OF_RULE,   3, 27),

            new Expectation("comment3",TokenType.IDENTIFIER, 4, 5),
            new Expectation(":==",     TokenType.ASIGN,   4, 14),
            new Expectation("literal3",TokenType.IDENTIFIER, 4, 18),
            new Expectation(".",       TokenType.END_OF_RULE,   4, 27),

            new Expectation("}",       TokenType.R_BRACE,   5, 1),
            new Expectation(null,        TokenType.EOF,        5, 1)
        ), "Assignemnt operators.");

        assertTokens(createSourceFromFixture("rules_with_literals.ebnf"), Arrays.asList(
            new Expectation("\"Rules with literal.\"", TokenType.LITERAL,    1, 1),
            new Expectation("{",                     TokenType.L_BRACE,   1, 23),

            new Expectation("literal",   TokenType.IDENTIFIER, 2, 5),
            new Expectation("=",         TokenType.ASIGN,   2, 13),
            new Expectation("\"\'\"",      TokenType.LITERAL,    2, 15),
            new Expectation("character", TokenType.IDENTIFIER, 2, 19),
            new Expectation("{",         TokenType.L_BRACE,   2, 29),
            new Expectation("character", TokenType.IDENTIFIER, 2, 31),
            new Expectation("}",         TokenType.R_BRACE,   2, 41),
            new Expectation("\"\'\"",      TokenType.LITERAL,    2, 43),

            new Expectation("|",         TokenType.CHOICE,   3, 13),
            new Expectation("'\"'",      TokenType.LITERAL,    3, 15),
            new Expectation("character", TokenType.IDENTIFIER, 3, 19),
            new Expectation("{",         TokenType.L_BRACE,   3, 29),
            new Expectation("character", TokenType.IDENTIFIER, 3, 31),
            new Expectation("}",         TokenType.R_BRACE,   3, 41),
            new Expectation("'\"'",      TokenType.LITERAL,    3, 43),
            new Expectation(".",         TokenType.END_OF_RULE,   3, 47),

            new Expectation("}",       TokenType.R_BRACE,   4, 1),
            new Expectation(null,        TokenType.EOF,        4, 1)
        ), "Rules with literal.");

        assertTokens(createSourceFromFixture("testgrammar_1.ebnf"), Arrays.asList(
            new Expectation("\"EBNF defined in itself.\"",   TokenType.LITERAL, 1, 1),
            new Expectation("{",          TokenType.L_BRACE,   1,  27),

            new Expectation("syntax",     TokenType.IDENTIFIER, 2,  3),
            new Expectation("=",          TokenType.ASIGN,   2,  14),
            new Expectation("[",          TokenType.L_BRACK,   2,  16),
            new Expectation("title",      TokenType.IDENTIFIER, 2,  18),
            new Expectation("]",          TokenType.R_BRACK,   2,  24),
            new Expectation("\"{\"",        TokenType.LITERAL,    2,  26),
            new Expectation("{",          TokenType.L_BRACE,   2,  30),
            new Expectation("rule",       TokenType.IDENTIFIER, 2,  32),
            new Expectation("}",          TokenType.R_BRACE,   2,  37),
            new Expectation("\"}\"",        TokenType.LITERAL,    2,  39),
            new Expectation("[",          TokenType.L_BRACK,   2,  43),
            new Expectation("comment",    TokenType.IDENTIFIER, 2,  45),
            new Expectation("]",          TokenType.R_BRACK,   2,  53),
            new Expectation(".",          TokenType.END_OF_RULE,   2,  55),

            new Expectation("rule",       TokenType.IDENTIFIER, 3,  3),
            new Expectation("=",          TokenType.ASIGN,   3,  14),
            new Expectation("identifier", TokenType.IDENTIFIER, 3,  16),
            new Expectation("(",          TokenType.L_PAREN,   3,  27),
            new Expectation("\"=\"",        TokenType.LITERAL,    3,  29),
            new Expectation("|",          TokenType.CHOICE,   3,  33),
            new Expectation("\":\"",        TokenType.LITERAL,    3,  35),
            new Expectation("|",          TokenType.CHOICE,   3,  39),
            new Expectation("\":==\"",      TokenType.LITERAL,    3,  41),
            new Expectation(")",          TokenType.R_PAREN,   3,  47),
            new Expectation("expression", TokenType.IDENTIFIER, 3,  49),
            new Expectation("(",          TokenType.L_PAREN,   3,  60),
            new Expectation("\".\"",        TokenType.LITERAL,    3,  62),
            new Expectation("|",          TokenType.CHOICE,   3,  66),
            new Expectation("\";\"",        TokenType.LITERAL,    3,  68),
            new Expectation(")",          TokenType.R_PAREN,   3,  72),
            new Expectation(".",          TokenType.END_OF_RULE,   3,  74),

            new Expectation("expression", TokenType.IDENTIFIER, 4,  3),
            new Expectation("=",          TokenType.ASIGN,   4,  14),
            new Expectation("term",       TokenType.IDENTIFIER, 4,  16),
            new Expectation("{",          TokenType.L_BRACE,   4,  21),
            new Expectation("\"|\"",        TokenType.LITERAL,    4,  23),
            new Expectation("term",       TokenType.IDENTIFIER, 4,  27),
            new Expectation("}",          TokenType.R_BRACE,   4,  32),
            new Expectation(".",          TokenType.END_OF_RULE,   4,  34),

            new Expectation("term",       TokenType.IDENTIFIER, 5,  3),
            new Expectation("=",          TokenType.ASIGN,   5,  14),
            new Expectation("factor",     TokenType.IDENTIFIER, 5,  16),
            new Expectation("{",          TokenType.L_BRACE,   5,  23),
            new Expectation("factor",     TokenType.IDENTIFIER, 5,  25),
            new Expectation("}",          TokenType.R_BRACE,   5,  32),
            new Expectation(".",          TokenType.END_OF_RULE,   5,  34),

            new Expectation("factor",     TokenType.IDENTIFIER, 6,  3),
            new Expectation("=",          TokenType.ASIGN,   6,  14),
            new Expectation("identifier", TokenType.IDENTIFIER, 6,  16),
            new Expectation("|",          TokenType.CHOICE,   7,  14),
            new Expectation("literal",    TokenType.IDENTIFIER, 7,  16),
            new Expectation("|",          TokenType.CHOICE,   8,  14),
            new Expectation("range",      TokenType.IDENTIFIER, 8,  16),
            new Expectation("|",          TokenType.CHOICE,   9,  14),
            new Expectation("\"[\"",        TokenType.LITERAL,    9,  16),
            new Expectation("expression", TokenType.IDENTIFIER, 9,  20),
            new Expectation("\"]\"",        TokenType.LITERAL,    9,  31),
            new Expectation("|",          TokenType.CHOICE,   10, 14),
            new Expectation("\"(\"",        TokenType.LITERAL,    10, 16),
            new Expectation("expression", TokenType.IDENTIFIER, 10, 20),
            new Expectation("\")\"",        TokenType.LITERAL,    10, 31),
            new Expectation("|",          TokenType.CHOICE,   11, 14),
            new Expectation("\"{\"",        TokenType.LITERAL,    11, 16),
            new Expectation("expression", TokenType.IDENTIFIER, 11, 20),
            new Expectation("\"}\"",        TokenType.LITERAL,    11, 31),
            new Expectation(".",          TokenType.END_OF_RULE,   11, 35),

            new Expectation("identifier", TokenType.IDENTIFIER, 12, 3),
            new Expectation("=",          TokenType.ASIGN,   12, 14),
            new Expectation("character",  TokenType.IDENTIFIER, 12, 16),
            new Expectation("{",          TokenType.L_BRACE,   12, 26),
            new Expectation("character",  TokenType.IDENTIFIER, 12, 28),
            new Expectation("}",          TokenType.R_BRACE,   12, 38),
            new Expectation(".",          TokenType.END_OF_RULE,   12, 40),

            new Expectation("range",      TokenType.IDENTIFIER, 13, 3),
            new Expectation("=",          TokenType.ASIGN,   13, 14),
            new Expectation("character",  TokenType.IDENTIFIER, 13, 16),
            new Expectation("\"..\"",       TokenType.LITERAL,    13, 26),
            new Expectation("character",  TokenType.IDENTIFIER, 13, 31),
            new Expectation(".",          TokenType.END_OF_RULE,   13, 41),

            new Expectation("title",      TokenType.IDENTIFIER, 14, 3),
            new Expectation("=",          TokenType.ASIGN,   14, 14),
            new Expectation("literal",    TokenType.IDENTIFIER, 14, 16),
            new Expectation(".",          TokenType.END_OF_RULE,   14, 24),

            new Expectation("comment",    TokenType.IDENTIFIER, 15, 3),
            new Expectation("=",          TokenType.ASIGN,   15, 14),
            new Expectation("literal",    TokenType.IDENTIFIER, 15, 16),
            new Expectation(".",          TokenType.END_OF_RULE,   15, 24),

            new Expectation("literal",    TokenType.IDENTIFIER, 16, 3),
            new Expectation("=",          TokenType.ASIGN,   16, 14),
            new Expectation("\"\'\"",       TokenType.LITERAL,    16, 16),
            new Expectation("character",  TokenType.IDENTIFIER, 16, 20),
            new Expectation("{",          TokenType.L_BRACE,   16, 30),
            new Expectation("character",  TokenType.IDENTIFIER, 16, 32),
            new Expectation("}",          TokenType.R_BRACE,   16, 42),
            new Expectation("\"\'\"",       TokenType.LITERAL,    16, 44),
            new Expectation("|",          TokenType.CHOICE,   17, 14),
            new Expectation("'\"'",       TokenType.LITERAL,    17, 16),
            new Expectation("character",  TokenType.IDENTIFIER, 17, 20),
            new Expectation("{",          TokenType.L_BRACE,   17, 30),
            new Expectation("character",  TokenType.IDENTIFIER, 17, 32),
            new Expectation("}",          TokenType.R_BRACE,   17, 42),
            new Expectation("'\"'",       TokenType.LITERAL,    17, 44),
            new Expectation(".",          TokenType.END_OF_RULE,   17, 48),

            new Expectation("character",  TokenType.IDENTIFIER, 18, 3),
            new Expectation("=",          TokenType.ASIGN,   18, 14),
            new Expectation("\"a\"",        TokenType.LITERAL,    18, 16),
            new Expectation("..",         TokenType.RANGE,   18, 20),
            new Expectation("\"z\"",        TokenType.LITERAL,    18, 23),
            new Expectation("|",          TokenType.CHOICE,   19, 14),
            new Expectation("\"A\"",        TokenType.LITERAL,    19, 16),
            new Expectation("..",         TokenType.RANGE,   19, 20),
            new Expectation("\"Z\"",        TokenType.LITERAL,    19, 23),
            new Expectation("|",          TokenType.CHOICE,   20, 14),
            new Expectation("\"0\"",        TokenType.LITERAL,    20, 16),
            new Expectation("..",         TokenType.RANGE,   20, 20),
            new Expectation("\"9\"",        TokenType.LITERAL,    20, 23),
            new Expectation(".",          TokenType.END_OF_RULE,   20, 27),
            new Expectation("}",          TokenType.R_BRACE,   21, 1),
            new Expectation(null,           TokenType.EOF,        21, 1)
        ), "testgrammar_1.ebnf");

        scanner = new Scanner(ReaderHelper.createFrom("\ncomment := literal .\n"));

        try {
            while (scanner.hasNextToken()) {
                scanner.nextToken();
            }

            fail("Expected exception not thrown!");
        } catch (SyntaxError ex) {
            assertEquals("Expecting '=' but seen ' '", ex.getMessage());
            Position pos = ex.getPosition();
            assertEquals(2, pos.getLine());
            assertEquals(11, pos.getColumn());
        }

        scanner.close();
    }

    @Ignore
    @Test public void testRaiseErrorOnInvalidCharacter() {
        // TODO: Throw excpetion on invalid characters.
    }

    @Test public void testRaiseError() {
        Position position = new Position(5, 3);
        Scanner scanner = mock(Scanner.class, Mockito.CALLS_REAL_METHODS);
        when(scanner.createPosition()).thenReturn(position);
        String msg = "an error";

        try {
            scanner.raiseError(msg);
            fail("Expected exception not thrown!");
        } catch (SyntaxError e) {
            verify(scanner, times(1)).createPosition();
            assertSame(e.getPosition(), position);
            assertEquals(String.format("Syntax error: %s at %s (code: 0)!", msg, position), e.
                    toString());
        }
    }

    @Test public void testPeekToken() throws SyntaxError, IOException {
        BufferedReader grammar = ReaderHelper.createFrom("comment :== literal .");
        Scanner scanner = new Scanner(grammar);
        scanner.nextToken();
        Token t;
        t = scanner.currentToken();
        assertEquals("comment", t.getValue());
        assertEquals(":==", scanner.peekToken().getValue());
        scanner.nextToken();
        t = scanner.currentToken();
        assertEquals(":==", t.getValue());
        assertEquals("literal", scanner.peekToken().getValue());
        scanner.nextToken();
        t = scanner.currentToken();
        assertEquals("literal", t.getValue());
        scanner.nextToken();
        t = scanner.currentToken();
        assertEquals(".", t.getValue());
        scanner.nextToken();
        t = scanner.currentToken();
        assertEquals(TokenType.EOF, t.getType());
    }
}
