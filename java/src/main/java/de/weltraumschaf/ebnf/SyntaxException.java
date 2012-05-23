package de.weltraumschaf.ebnf;

/**
 * Exception for signaling syntax errors.
 *
 * Provides a {@link Position} where in the input stream the syntax error occurred.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SyntaxException extends EbnfException {

    /**
     * Default exception code.
     */
    public static final int DEFAULT_CODE = 0;
    private static final long serialVersionUID = 1L;

    /**
     * Where in the source the exception occurred.
     */
    private final Position position;

    /**
     * Initializes error without cause and default error code.
     *
     * @param message The error message.
     * @param pos     Where the error occurred.
     */
    public SyntaxException(final String message, final Position pos) {
        this(message, pos, DEFAULT_CODE);
    }

    /**
     * Initializes error without cause.
     *
     * @param message The error message.
     * @param pos     Where the error occurred.
     * @param code    Optional error code.
     */
    public SyntaxException(final String message, final Position pos, final int code) {
        this(message, pos, code, null);
    }

    /**
     * Dedicated constructor.
     *
     * @param message The error message.
     * @param pos     Where the error occurred.
     * @param code    Optional error code.
     * @param cause   Optional previous exception.
     */
    public SyntaxException(final String message, final Position pos, final int code, final Throwable cause) {
        super(message, code, cause);
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
           .append(" (code: ")
           .append(getCode())
           .append(")!");
        return str.toString();
    }

}
