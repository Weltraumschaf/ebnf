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

import java.io.*;
import java.net.URI;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Factory {

    private Factory() { }

    public static Scanner newScanner(Reader reader) {
        return newScanner(reader, null);
    }

    public static Scanner newScanner(Reader reader, String fileName) {
        return new EbnfScanner(reader, fileName);
    }

    public static Parser newParser(Scanner scanner) {
        return new EbnfParser(scanner);
    }

    public static Parser newParserFromSource(final URI uri) throws FileNotFoundException {
        return newParserFromSource(uri, null);
    }

    public static Parser newParserFromSource(final URI uri, final String fileName) throws FileNotFoundException {
        return newParserFromSource(new File(uri), fileName);
    }

    public static Parser newParserFromSource(final File file) throws FileNotFoundException {
        return newParserFromSource(file, null);
    }

    public static Parser newParserFromSource(final File file, final String fileName) throws FileNotFoundException {
        return newParserFromSource(new FileReader(file), fileName);
    }

    public static Parser newParserFromSource(final String src) {
        return newParserFromSource(src, null);
    }

    public static Parser newParserFromSource(final String src, final String fileName) {
        return newParserFromSource(new StringReader(src), fileName);
    }

    public static Parser newParserFromSource(final Reader reader) {
        return newParserFromSource(reader, null);
    }

    public static Parser newParserFromSource(final Reader reader, final String fileName) {
        final Scanner scanner = newScanner(new BufferedReader(reader), fileName);
        return newParser(scanner);
    }
}
