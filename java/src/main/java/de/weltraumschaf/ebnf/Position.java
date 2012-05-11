package de.weltraumschaf.ebnf;

/**
 * Represents a token position in the source string.
 *
 * The position contains the line, column and filename where the
 * token occurred. The file name is optional.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Position {

    /**
     * File of the source string.
     */
    private String file;
    /**
     * Line of occurrence.
     */
    private int line;
    /**
     * Column of occurrence.
     */
    private int column;

    /**
     * Initializes without file.
     *
     * @param line Line of occurrence.
     * @param column Column of occurrence.
     */
    public Position(final int line, final int column) {
        this(line, column, null);
    }

    /**
     * Dedicated constructor initializes immutable object.
     *
     * File is optional e.g. if string is parsed directly without any file.
     *
     * @param line Line of occurrence.
     * @param column Column of occurrence.
     * @param file Optional file name.
     */
    public Position(final int line, final int column, final String file) {
        this.line   = line;
        this.column = column;
        this.file   = file;
    }

    /**
     * Returns the file name of the source.
     *
     * May be null.
     *
     * @return
     */
    public String getFile() {
        return file;
    }

    /**
     * Returns line of occurence in source.
     *
     * @return
     */
    public int getLine() {
        return line;
    }

    /**
     * Returns column of occurence in source.
     *
     * @return
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns human readable string representation.
     *
     * @return string
     */
    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();

        if (getFile() != null) {
            str.append(getFile()).append(' ');
        }

        str.append('(').append(getLine()).append(", ").append(getColumn()).append(')');

        return str.toString();
    }
}
