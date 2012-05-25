package de.weltraumschaf.ebnf.parser;

/**
 * Exception for signaling syntax errors.
 *
 * Provides a {@link Position} where in the input stream the syntax error occurred.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SyntaxException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Where in the source the exception occurred.
     */
    private final Position position;

    /**
     * Initializes error without cause.
     *
     * @param message The error message.
     * @param pos     Where the error occurred.
     */
    public SyntaxException(final String message, final Position pos) {
        this(message, pos, null);
    }

    /**
     * Dedicated constructor.
     *
     * @param message The error message.
     * @param pos     Where the error occurred.
     * @param cause   Optional previous exception.
     */
    public SyntaxException(final String message, final Position pos, final Throwable cause) {
        super(message, cause);
        position = pos;
    }

    /**
     * Returns the position where the error occurred.
     *
     * @return
     */
    public final Position getPosition() {
        return position;
    }

    /**
     * Human readable representation.
     *
     * @return
     */
    @Override
    public final String toString() {
        final StringBuilder str = new StringBuilder("Syntax error: ");
        str.append(getMessage())
           .append(" at ")
           .append(position)
           .append("!");
        return str.toString();
    }

}
