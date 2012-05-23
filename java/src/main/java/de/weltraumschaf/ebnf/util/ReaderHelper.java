package de.weltraumschaf.ebnf.util;

import java.io.*;
import java.net.URI;

/**
 * Helper to get {@link BufferedReader} objects from various sources.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class ReaderHelper {

    private ReaderHelper() { }

    /**
     * Creates a source file from the file specified by an {@link URI} object.
     *
     * @param uri The resource URI.
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedReader createFrom(final URI uri) throws FileNotFoundException {
        return createFrom(new File(uri));
    }

    /**
     * Creates source from given file.
     *
     * @param file The file to read from.
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedReader createFrom(final File file) throws FileNotFoundException {
        return createFrom(new FileReader(file));
    }

    /**
     * Creates a source file from given string.
     *
     * @param src The source as string literal.
     * @return
     */
    public static BufferedReader createFrom(final String src) {
        return createFrom(new StringReader(src));
    }

    /**
     * Creates buffered reader from a given reader.
     *
     * @param reader The reader.
     * @return
     */
    public static BufferedReader createFrom(final Reader reader) {
        return new BufferedReader(reader);
    }

}
