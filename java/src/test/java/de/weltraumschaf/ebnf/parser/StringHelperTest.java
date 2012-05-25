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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class StringHelperTest {

    @Test  public void testUnquoteString() {
        assertEquals("a test string", StringHelper.unquoteString("\"a test string\""));
        assertEquals("a \"test\" string", StringHelper.unquoteString("\"a \"test\" string\"")); // NOPMD
        assertEquals("a \"test\" string", StringHelper.unquoteString("\"a \"test\" string\""));
        assertEquals("a test string", StringHelper.unquoteString("'a test string'"));
        assertEquals("a 'test' string", StringHelper.unquoteString("'a 'test' string'"));
        assertEquals("a 'test' string", StringHelper.unquoteString("'a \'test\' string'"));
    }

}
