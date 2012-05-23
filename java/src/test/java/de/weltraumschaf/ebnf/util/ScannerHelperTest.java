package de.weltraumschaf.ebnf.util;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit test for ScannerHelper.
 */
public class ScannerHelperTest {

    private static final char[] OPS =
        {'(', ')', '[', ']', '{', '}', '=', '.', ';', '|', ',', '-', ':'};

    private static final char[] LOW_ALPHA =
        {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
         'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    private static final char[] UP_ALPHA =
        {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
         'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    private static final char[] NUMS = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
    
    private static final char[] WHITE_SPACES = {' ', '\n', '\r', '\t'};

    @Test public void isAlpha() {
        for (int i = 0; i < LOW_ALPHA.length; ++i) {
            assertTrue(ScannerHelper.isAlpha(LOW_ALPHA[i]));
        }

        for (int i = 0; i < UP_ALPHA.length; ++i) {
            assertTrue(ScannerHelper.isAlpha(UP_ALPHA[i]));
        }

        for (int i = 0; i < NUMS.length; ++i) {
            assertFalse(ScannerHelper.isAlpha(NUMS[i]));
        }

        for (int i = 0; i < OPS.length; ++i) {
            assertFalse(ScannerHelper.isAlpha(OPS[i]));
        }

        for (int i = 0; i < WHITE_SPACES.length; ++i) {
            assertFalse(ScannerHelper.isAlpha(WHITE_SPACES[i]));
        }
    }

    @Test public void isNum() {
        for (int i = 0; i < NUMS.length; ++i) {
            assertTrue(ScannerHelper.isNum(NUMS[i]));
        }

        for (int i = 0; i < OPS.length; ++i) {
            assertFalse(ScannerHelper.isNum(OPS[i]));
        }

        for (int i = 0; i < LOW_ALPHA.length; ++i) {
            assertFalse(ScannerHelper.isNum(LOW_ALPHA[i]));
        }

        for (int i = 0; i < UP_ALPHA.length; ++i) {
            assertFalse(ScannerHelper.isNum(UP_ALPHA[i]));
        }

        for (int i = 0; i < WHITE_SPACES.length; ++i) {
            assertFalse(ScannerHelper.isNum(WHITE_SPACES[i]));
        }
    }

    @Test public void isAlphaNum() {
        for (int i = 0; i < NUMS.length; ++i) {
            assertTrue(ScannerHelper.isAlphaNum(NUMS[i]));
        }

        for (int i = 0; i < LOW_ALPHA.length; ++i) {
            assertTrue(ScannerHelper.isAlphaNum(LOW_ALPHA[i]));
        }

        for (int i = 0; i < UP_ALPHA.length; ++i) {
            assertTrue(ScannerHelper.isAlphaNum(UP_ALPHA[i]));
        }

        for (int i = 0; i < OPS.length; ++i) {
            assertFalse(ScannerHelper.isAlphaNum(OPS[i]));
        }

        for (int i = 0; i < WHITE_SPACES.length; ++i) {
            assertFalse(ScannerHelper.isAlphaNum(WHITE_SPACES[i]));
        }
    }

    @Test public void isOperator() {
        for (int i = 0; i < OPS.length; ++i) {
            assertTrue(ScannerHelper.isOperator(OPS[i]));
        }

        for (int i = 0; i < NUMS.length; ++i) {
            assertFalse(ScannerHelper.isOperator(NUMS[i]));
        }

        for (int i = 0; i < LOW_ALPHA.length; ++i) {
            assertFalse(ScannerHelper.isOperator(LOW_ALPHA[i]));
        }

        for (int i = 0; i < UP_ALPHA.length; ++i) {
            assertFalse(ScannerHelper.isOperator(UP_ALPHA[i]));
        }

        for (int i = 0; i < WHITE_SPACES.length; ++i) {
            assertFalse(ScannerHelper.isOperator(WHITE_SPACES[i]));
        }
    }

    @Test public void isWhiteSpace() {
        for (int i = 0; i < WHITE_SPACES.length; ++i) {
            assertTrue(ScannerHelper.isWhiteSpace(WHITE_SPACES[i]));
        }

        for (int i = 0; i < OPS.length; ++i) {
            assertFalse(ScannerHelper.isWhiteSpace(OPS[i]));
        }

        for (int i = 0; i < NUMS.length; ++i) {
            assertFalse(ScannerHelper.isWhiteSpace(NUMS[i]));
        }

        for (int i = 0; i < LOW_ALPHA.length; ++i) {
            assertFalse(ScannerHelper.isWhiteSpace(LOW_ALPHA[i]));
        }

        for (int i = 0; i < UP_ALPHA.length; ++i) {
            assertFalse(ScannerHelper.isWhiteSpace(UP_ALPHA[i]));
        }
    }

    @Test public void isQuote() {
        assertTrue(ScannerHelper.isQuote('"'));
        assertTrue(ScannerHelper.isQuote('\''));

        for (int i = 0; i < WHITE_SPACES.length; ++i) {
            assertFalse(ScannerHelper.isQuote(WHITE_SPACES[i]));
        }

        for (int i = 0; i < OPS.length; ++i) {
            assertFalse(ScannerHelper.isQuote(OPS[i]));
        }

        for (int i = 0; i < NUMS.length; ++i) {
            assertFalse(ScannerHelper.isQuote(NUMS[i]));
        }

        for (int i = 0; i < LOW_ALPHA.length; ++i) {
            assertFalse(ScannerHelper.isQuote(LOW_ALPHA[i]));
        }

        for (int i = 0; i < UP_ALPHA.length; ++i) {
            assertFalse(ScannerHelper.isQuote(UP_ALPHA[i]));
        }
    }

    @Test public void isEquals() {
        final char[] chars = {'-', '_'};
        assertTrue(ScannerHelper.isEquals('-', chars));
        assertTrue(ScannerHelper.isEquals('_', chars));
        assertFalse(ScannerHelper.isEquals('a', chars));
    }

    @Test public void isCharInRange() {
        assertTrue(ScannerHelper.isCharInRange('c', 'a', 'f'));
        assertTrue(ScannerHelper.isCharInRange('3', '1', '5'));
        assertFalse(ScannerHelper.isCharInRange('b', 'f', 'z'));
        assertFalse(ScannerHelper.isCharInRange('3', '7', '9'));

        try {
            ScannerHelper.isCharInRange('c', 'f', 'a');
            fail("Expected exception not thrown!");
        } catch (IllegalArgumentException ex) {
            assertEquals("End must be greater or equal than start!", ex.getMessage());
        }
    }
}
