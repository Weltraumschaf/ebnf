package de.weltraumschaf.ebnf;

import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for Token.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class TokenTest {

    @Test public void testGetTypeAsString() {
        final Position pos = new Position(5, 10);
        Token token;
        token = new Token(TokenType.IDENTIFIER, "", pos);
        assertEquals("IDENTIFIER", token.getType().toString());
        token = new Token(TokenType.COMMENT, "", pos);
        assertEquals("COMMENT", token.getType().toString());
        token = new Token(TokenType.LITERAL, "", pos);
        assertEquals("LITERAL", token.getType().toString());
        token = new Token(TokenType.EOF, "", pos);
        assertEquals("EOF", token.getType().toString());
        token = new Token(TokenType.L_BRACK, "", pos);
        assertEquals("L_BRACK", token.getType().toString());
    }

    @Test public void testToString() {
        Position pos = new Position(5, 10);
        Token token;
        token = new Token(TokenType.IDENTIFIER, "ident", pos);
        assertEquals("<'ident', IDENTIFIER, (5, 10)>", token.toString());

        pos = new Position(5, 10, "/foo/bar.ebnf");
        token = new Token(TokenType.IDENTIFIER, "ident", pos);
        assertEquals("<'ident', IDENTIFIER, /foo/bar.ebnf (5, 10)>", token.toString());

        pos = new Position(5, 10, "/foo/bar.ebnf");
        token = new Token(TokenType.IDENTIFIER, "", pos);
        assertEquals("<IDENTIFIER, /foo/bar.ebnf (5, 10)>", token.toString());

        pos = new Position(5, 10, "/foo/bar.ebnf");
        token = new Token(TokenType.IDENTIFIER, "a-very-very-very-very-long-identifier", pos);
        assertEquals("<'a-very-very-ver...', IDENTIFIER, /foo/bar.ebnf (5, 10)>", token.toString());
    }

    @Test public void testIsType() {
        final Position pos = new Position(5, 10);
        Token token;

        token = new Token(TokenType.L_BRACK, "", pos);
        assertTrue(token.isType(TokenType.L_BRACK));
        assertFalse(token.isType(TokenType.IDENTIFIER));
        assertFalse(token.isType(TokenType.COMMENT));
        assertFalse(token.isType(TokenType.LITERAL));
        assertFalse(token.isType(TokenType.EOF));

        token = new Token(TokenType.IDENTIFIER, "", pos);
        assertTrue(token.isType(TokenType.IDENTIFIER));
        assertFalse(token.isType(TokenType.L_BRACK));
        assertFalse(token.isType(TokenType.COMMENT));
        assertFalse(token.isType(TokenType.LITERAL));
        assertFalse(token.isType(TokenType.EOF));

        token = new Token(TokenType.COMMENT, "", pos);
        assertTrue(token.isType(TokenType.COMMENT));
        assertFalse(token.isType(TokenType.IDENTIFIER));
        assertFalse(token.isType(TokenType.L_BRACK));
        assertFalse(token.isType(TokenType.LITERAL));
        assertFalse(token.isType(TokenType.EOF));

        token = new Token(TokenType.LITERAL, "", pos);
        assertTrue(token.isType(TokenType.LITERAL));
        assertFalse(token.isType(TokenType.IDENTIFIER));
        assertFalse(token.isType(TokenType.COMMENT));
        assertFalse(token.isType(TokenType.L_BRACK));
        assertFalse(token.isType(TokenType.EOF));

        token = new Token(TokenType.EOF, "", pos);
        assertTrue(token.isType(TokenType.EOF));
        assertFalse(token.isType(TokenType.IDENTIFIER));
        assertFalse(token.isType(TokenType.COMMENT));
        assertFalse(token.isType(TokenType.LITERAL));
        assertFalse(token.isType(TokenType.L_BRACK));
    }

    @Test public void testIsEqual() {
        final Position pos = new Position(5, 10);
        final Token token = new Token(TokenType.LITERAL, "foo", pos);
        assertTrue(token.isEqual("foo"));
        assertFalse(token.isEqual("bar"));

        assertFalse(token.isNotEqual("foo"));
        assertTrue(token.isNotEqual("bar"));
    }

    @Test public void testIsEquals() {
        final List<Token> tokens = Arrays.asList(
            new Token(TokenType.IDENTIFIER, "a", new Position(0, 0)),
            new Token(TokenType.IDENTIFIER, "b", new Position(0, 0)),
            new Token(TokenType.IDENTIFIER, "c", new Position(0, 0))
        );

        final String[] set1 = {"a", "b", "c"};
        final String[] set2 = {"x", "y", "z"};
        for (Token token : tokens) {
            assertTrue(token.isEquals(set1));
            assertFalse(token.isEquals(set2));
            assertFalse(token.isNotEquals(set1));
            assertTrue(token.isNotEquals(set2));
        }
    }

   @Test  public void testUnquoteString() {
        assertEquals("a test string", Token.unquoteString("\"a test string\""));
        assertEquals("a \"test\" string", Token.unquoteString("\"a \"test\" string\"")); // NOPMD
        assertEquals("a \"test\" string", Token.unquoteString("\"a \"test\" string\""));
        assertEquals("a test string", Token.unquoteString("'a test string'"));
        assertEquals("a 'test' string", Token.unquoteString("'a 'test' string'"));
        assertEquals("a 'test' string", Token.unquoteString("'a \'test\' string'"));

        final Token token = new Token(TokenType.COMMENT, "\"a \"test\" string\"", new Position(0, 0));
        assertEquals("\"a \"test\" string\"", token.getValue());
        assertEquals("a \"test\" string", token.getValue(true));
    }

    @Test public void testGetPostion() {
        final Token token = new Token(TokenType.IDENTIFIER, "abc", new Position(1, 5));
        final Position end = token.getPosition(true);
        assertEquals(1, end.getLine());
        assertEquals(8, end.getColumn());
    }

    @Test public void testIsOperator() {
        List<TokenType> types;

        types = Arrays.asList(
            TokenType.ASIGN, TokenType.CHOICE, TokenType.END_OF_RULE, TokenType.L_BRACE, TokenType.L_BRACK,
            TokenType.L_PAREN, TokenType.RANGE, TokenType.R_BRACE, TokenType.R_BRACK, TokenType.R_PAREN
        );

        final Position pos = new Position(0, 0);
        for (TokenType type : types) {
            final Token token = new Token(type, "", pos); // NOPMD
            assertTrue(token.isOperator());
        }

        types = Arrays.asList(
            TokenType.COMMENT, TokenType.EOF, TokenType.IDENTIFIER, TokenType.LITERAL
        );

        for (TokenType type : types) {
            Token token = new Token(type, "", pos); // NOPMD
            assertFalse(token.isOperator());
        }
    }
}
