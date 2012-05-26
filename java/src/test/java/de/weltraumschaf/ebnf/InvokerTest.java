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

import de.weltraumschaf.ebnf.cli.CliOptions;
import java.io.PrintStream;
import org.apache.commons.cli.ParseException;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class InvokerTest {

    @Test public void println() {
        final PrintStream out = mock(PrintStream.class);
        final Invoker sut = new Invoker(new String[]{"foo"}, out);
        final String msg = "some text";
        sut.println(msg);
        verify(out, times(1)).println(msg);
    }

    @Test public void parseOptions() throws EbnfException, ParseException {
        final CliOptions options = mock(CliOptions.class);
        final String[] args = new String[]{"-d", "-h"};
        final Invoker sut = new Invoker(args, mock(PrintStream.class), options);
        final CliOptions parsedOptions = sut.parseOptions();
        assertSame(options, parsedOptions);
        verify(options, times(1)).parse(args);
    }

    @Test public void parseOptionsWithPArseError() throws ParseException {
        final CliOptions options = mock(CliOptions.class);
        final String[] args = new String[]{"foo"};
        final Invoker sut = new Invoker(args, mock(PrintStream.class), options);
        final ParseException throwed = new ParseException("foobar");
        doThrow(throwed).when(options).parse(args);

        try {
            sut.parseOptions();
            fail("Expected exeption not thrown!");
        } catch (EbnfException ex) {
            assertEquals(throwed.getMessage(), ex.getMessage());
            assertSame(throwed, ex.getCause());
        }

        verify(options, times(1)).parse(args);
    }
}
