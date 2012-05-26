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

package de.weltraumschaf.ebnf.cli;

import de.weltraumschaf.ebnf.OutputFormat;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CliOptionsTest {

    private CliOptions setUpSut(final String[] args) throws ParseException {
        final CliOptions options = new CliOptions();
        options.parse(args);
        return options;
    }

    @Test public void defaults() {
        final CliOptions options = new CliOptions();
        assertFalse(options.isDebug());
        assertFalse(options.isHelp());
        assertFalse(options.isTextTree());
        assertFalse(options.isIde());
        assertFalse(options.hasOutputFile());
        assertFalse(options.hasSyntaxFile());
        assertFalse(options.hasOutputFormat());
    }

    @Test public void parseNoArgOptions() throws ParseException {
        CliOptions options = setUpSut(new String[] {"-d"});
        assertTrue(options.isDebug());
        assertFalse(options.isHelp());
        assertFalse(options.isTextTree());
        assertFalse(options.isIde());

        options = setUpSut(new String[] {"-h"});
        assertTrue(options.isHelp());
        assertFalse(options.isDebug());
        assertFalse(options.isTextTree());
        assertFalse(options.isIde());

        options = setUpSut(new String[] {"-t"});
        assertTrue(options.isTextTree());
        assertFalse(options.isDebug());
        assertFalse(options.isHelp());
        assertFalse(options.isIde());

        options = setUpSut(new String[] {"-i"});
        assertTrue(options.isIde());
        assertFalse(options.isTextTree());
        assertFalse(options.isDebug());
        assertFalse(options.isHelp());

        options = setUpSut(new String[] {"--ide"});
        assertTrue(options.isIde());
        assertFalse(options.isTextTree());
        assertFalse(options.isDebug());
        assertFalse(options.isHelp());
    }

    @Test public void parseArgOptions() throws ParseException {
        CliOptions options = setUpSut(new String[] {"-s", "foo"});
        assertTrue(options.hasSyntaxFile());
        assertEquals("foo", options.getSyntaxFile());

        options = setUpSut(new String[] {"-o", "bar"});
        assertTrue(options.hasOutputFile());
        assertEquals("bar", options.getOutputFile());
        assertFalse(options.hasSyntaxFile());

        options = setUpSut(new String[] {"-s", "foo.ebnf", "-f", "xml"});
        assertTrue(options.hasOutputFile());
        assertEquals("foo.xml", options.getOutputFile());
    }

    @Test public void parseOutputFormatOptions() throws ParseException {
        CliOptions options = setUpSut(new String[] {"-f", "xml"});
        assertTrue(options.hasOutputFormat());
        assertEquals(OutputFormat.XML, options.getOutputFormat());

        options = setUpSut(new String[] {"-f", "jpg"});
        assertTrue(options.hasOutputFormat());
        assertEquals(OutputFormat.JPG, options.getOutputFormat());

        options = setUpSut(new String[] {"-f", "gif"});
        assertTrue(options.hasOutputFormat());
        assertEquals(OutputFormat.GIF, options.getOutputFormat());

        options = setUpSut(new String[] {"-f", "foobar"});
        assertTrue(options.hasOutputFormat());
        assertEquals(OutputFormat.JPG, options.getOutputFormat());
    }

    @Test public void format() {
        final HelpFormatter formatter = mock(HelpFormatter.class);
        final CliOptions options = new CliOptions();
        options.format(formatter);
        verify(formatter, times(1)).printHelp("ebnf", options.options);
    }
}
