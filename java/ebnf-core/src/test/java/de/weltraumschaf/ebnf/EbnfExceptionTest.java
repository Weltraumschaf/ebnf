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

package de.weltraumschaf.ebnf;

import java.io.IOException;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class EbnfExceptionTest {

    @Test public void creation() {
        EbnfException sut = new EbnfException("foo");
        assertEquals("foo", sut.getMessage());
        assertEquals(ExitCode.FATAL_ERROR.getCode(), sut.getCode());
        assertNull(sut.getCause());

        sut = new EbnfException("bar", ExitCode.NO_SYNTAX);
        assertEquals("bar", sut.getMessage());
        assertEquals(ExitCode.NO_SYNTAX.getCode(), sut.getCode());
        assertNull(sut.getCause());

        sut = new EbnfException("baz", 5);
        assertEquals("baz", sut.getMessage());
        assertEquals(5, sut.getCode());
        assertNull(sut.getCause());

        final Throwable cause = new IOException();
        sut = new EbnfException("snafu", 5, cause);
        assertEquals("snafu", sut.getMessage());
        assertEquals(5, sut.getCode());
        assertSame(cause, sut.getCause());

        sut = new EbnfException("bla", ExitCode.NO_SYNTAX, cause);
        assertEquals("bla", sut.getMessage());
        assertEquals(ExitCode.NO_SYNTAX.getCode(), sut.getCode());
        assertSame(cause, sut.getCause());
    }
}
