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
        Position p = new Position(5, 10);
        Token t;
        t = new Token(TokenType.IDENTIFIER, "", p);
        assertEquals("IDENTIFIER", t.getType().toString());
        t = new Token(TokenType.COMMENT, "", p);
        assertEquals("COMMENT", t.getType().toString());
        t = new Token(TokenType.LITERAL, "", p);
        assertEquals("LITERAL", t.getType().toString());
        t = new Token(TokenType.EOF, "", p);
        assertEquals("EOF", t.getType().toString());
        t = new Token(TokenType.L_BRACK, "", p);
        assertEquals("L_BRACK", t.getType().toString());
    }

    @Test public void testToString() {
        Position p = new Position(5, 10);
        Token t;
        t = new Token(TokenType.IDENTIFIER, "ident", p);
        assertEquals("<'ident', IDENTIFIER, (5, 10)>", t.toString());

        p = new Position(5, 10, "/foo/bar.ebnf");
        t = new Token(TokenType.IDENTIFIER, "ident", p);
        assertEquals("<'ident', IDENTIFIER, /foo/bar.ebnf (5, 10)>", t.toString());

        p = new Position(5, 10, "/foo/bar.ebnf");
        t = new Token(TokenType.IDENTIFIER, "", p);
        assertEquals("<IDENTIFIER, /foo/bar.ebnf (5, 10)>", t.toString());

        p = new Position(5, 10, "/foo/bar.ebnf");
        t = new Token(TokenType.IDENTIFIER, "a-very-very-very-very-long-identifier", p);
        assertEquals("<'a-very-very-ver...', IDENTIFIER, /foo/bar.ebnf (5, 10)>", t.toString());
    }

    @Test public void testIsType() {
        Position p = new Position(5, 10);
        Token t;

        t = new Token(TokenType.L_BRACK, "", p);
        assertTrue(t.isType(TokenType.L_BRACK));
        assertFalse(t.isType(TokenType.IDENTIFIER));
        assertFalse(t.isType(TokenType.COMMENT));
        assertFalse(t.isType(TokenType.LITERAL));
        assertFalse(t.isType(TokenType.EOF));

        t = new Token(TokenType.IDENTIFIER, "", p);
        assertTrue(t.isType(TokenType.IDENTIFIER));
        assertFalse(t.isType(TokenType.L_BRACK));
        assertFalse(t.isType(TokenType.COMMENT));
        assertFalse(t.isType(TokenType.LITERAL));
        assertFalse(t.isType(TokenType.EOF));

        t = new Token(TokenType.COMMENT, "", p);
        assertTrue(t.isType(TokenType.COMMENT));
        assertFalse(t.isType(TokenType.IDENTIFIER));
        assertFalse(t.isType(TokenType.L_BRACK));
        assertFalse(t.isType(TokenType.LITERAL));
        assertFalse(t.isType(TokenType.EOF));

        t = new Token(TokenType.LITERAL, "", p);
        assertTrue(t.isType(TokenType.LITERAL));
        assertFalse(t.isType(TokenType.IDENTIFIER));
        assertFalse(t.isType(TokenType.COMMENT));
        assertFalse(t.isType(TokenType.L_BRACK));
        assertFalse(t.isType(TokenType.EOF));

        t = new Token(TokenType.EOF, "", p);
        assertTrue(t.isType(TokenType.EOF));
        assertFalse(t.isType(TokenType.IDENTIFIER));
        assertFalse(t.isType(TokenType.COMMENT));
        assertFalse(t.isType(TokenType.LITERAL));
        assertFalse(t.isType(TokenType.L_BRACK));
    }

    @Test public void testIsEqual() {
        Position p = new Position(5, 10);
        Token t = new Token(TokenType.LITERAL, "foo", p);
        assertTrue(t.isEqual("foo"));
        assertFalse(t.isEqual("bar"));

        assertFalse(t.isNotEqual("foo"));
        assertTrue(t.isNotEqual("bar"));
    }

    @Test public void testIsEquals() {
        List<Token> tokens = Arrays.asList(
            new Token(TokenType.IDENTIFIER, "a", new Position(0, 0)),
            new Token(TokenType.IDENTIFIER, "b", new Position(0, 0)),
            new Token(TokenType.IDENTIFIER, "c", new Position(0, 0))
        );

        String[] set1 = {"a", "b", "c"};
        String[] set2 = {"x", "y", "z"};
        for (Token token : tokens) {
            assertTrue(token.isEquals(set1));
            assertFalse(token.isEquals(set2));
            assertFalse(token.isNotEquals(set1));
            assertTrue(token.isNotEquals(set2));
        }
    }

   @Test  public void testUnquoteString() {
        assertEquals("a test string", Token.unquoteString("\"a test string\""));
        assertEquals("a \"test\" string", Token.unquoteString("\"a \"test\" string\""));
        assertEquals("a \"test\" string", Token.unquoteString("\"a \"test\" string\""));
        assertEquals("a test string", Token.unquoteString("'a test string'"));
        assertEquals("a 'test' string", Token.unquoteString("'a 'test' string'"));
        assertEquals("a 'test' string", Token.unquoteString("'a \'test\' string'"));

        Token t = new Token(TokenType.COMMENT, "\"a \"test\" string\"", new Position(0, 0));
        assertEquals("\"a \"test\" string\"", t.getValue());
        assertEquals("a \"test\" string", t.getValue(true));
    }

    @Test public void testGetPostion() {
        Token t = new Token(TokenType.IDENTIFIER, "abc", new Position(1, 5));
        Position end = t.getPosition(true);
        assertEquals(1, end.getLine());
        assertEquals(8, end.getColumn());
    }

    @Test public void testIsOperator() {
        List<TokenType> types;

        types = Arrays.asList(
            TokenType.ASIGN, TokenType.CHOICE, TokenType.END_OF_RULE, TokenType.L_BRACE, TokenType.L_BRACK,
            TokenType.L_PAREN, TokenType.RANGE, TokenType.R_BRACE, TokenType.R_BRACK, TokenType.R_PAREN
        );

        for (TokenType type : types) {
            Token token = new Token(type, "", new Position(0, 0));
            assertTrue(token.isOperator());
        }

        types = Arrays.asList(
            TokenType.COMMENT, TokenType.EOF, TokenType.IDENTIFIER, TokenType.LITERAL
        );

        for (TokenType type : types) {
            Token token = new Token(type, "", new Position(0, 0));
            assertFalse(token.isOperator());
        }
    }
}
