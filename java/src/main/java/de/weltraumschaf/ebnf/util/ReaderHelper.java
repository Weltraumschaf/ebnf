package de.weltraumschaf.ebnf.util;

import java.io.*;
import java.net.URI;

/**
 * Helper to get {@link BufferedReader} objects from various sources.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class ReaderHelper {

    /**
     * Creates a source file from the file specified by an {@link URI} object.
     *
     * @param uri The resource URI.
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedReader createFrom(URI uri) throws FileNotFoundException {
        return createFrom(new File(uri));
    }

    /**
     * Creates source from given file.
     *
     * @param file The file to read from.
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedReader createFrom(File file) throws FileNotFoundException {
        return createFrom(new FileReader(file));
    }

    /**
     * Creates a source file from given string.
     *
     * @param src The source as string literal.
     * @return
     */
    public static BufferedReader createFrom(String src) {
        return createFrom(new StringReader(src));
    }

    /**
     * Creates buffered reader from a given reader.
     *
     * @param r The reader.
     * @return
     */
    public static BufferedReader createFrom(Reader r) {
        return new BufferedReader(r);
    }

}
