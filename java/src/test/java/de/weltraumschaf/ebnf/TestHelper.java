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

import com.google.common.io.Files;
import de.weltraumschaf.ebnf.parser.Factory;
import de.weltraumschaf.ebnf.parser.Parser;
import de.weltraumschaf.ebnf.parser.Scanner;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class TestHelper {

    public static final String FIXTURE_DIR = "/de/weltraumschaf/ebnf";

    private static final TestHelper INSTANCE = new TestHelper();


    private TestHelper() {

    }

    public static TestHelper helper() {
        return INSTANCE;
    }

    private Reader createFileReaderFromFixture(final String fixtureFile) throws  FileNotFoundException, URISyntaxException {
        return new FileReader(new File(createResourceFromFixture(fixtureFile)));
    }

    private URI createResourceFromFixture(final String fixtureFile) throws URISyntaxException {
        return getClass().getResource(FIXTURE_DIR + '/' + fixtureFile).toURI();
    }

    public String createStringFromFixture(final String fixtureFile) throws URISyntaxException, IOException {
        return Files.toString(new File(createResourceFromFixture(fixtureFile)), Charset.defaultCharset());
    }

    public Scanner createScannerFromFixture(final String fixtureFile) throws FileNotFoundException, URISyntaxException {
        return Factory.newScanner(createFileReaderFromFixture(fixtureFile));
    }

    public Parser createParserFromFixture(final String fixtureFile) throws FileNotFoundException, URISyntaxException {
        return Factory.newParserFromSource(createResourceFromFixture(fixtureFile));
    }
}
