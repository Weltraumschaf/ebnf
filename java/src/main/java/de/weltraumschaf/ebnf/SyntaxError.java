package de.weltraumschaf.ebnf;

/**
 * Exception for signaling syntax errors.
 *
 * Provides a {@link Position} where in the input stream the syntax error occurred.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SyntaxError extends Error {

    public static final int DEFAULT_CODE = 0;

    /**
     * Where in the source the exception occurred.
     */
    private Position position;

    /**
     * Initializes error without cause and default error code.
     *
     * @param message The error message.
     * @param pos     Where the error occurred.
     */
    public SyntaxError(String message, Position pos) {
        this(message, pos, DEFAULT_CODE);
    }

    /**
     * Initializes error without cause.
     *
     * @param message The error message.
     * @param pos     Where the error occurred.
     * @param code    Optional error code.
     */
    public SyntaxError(String message, Position pos, int code) {
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
    public SyntaxError(String message, Position pos, int code, Throwable cause) {
        super(message, code, cause);
        position = pos;
    }

    /**
     * Returns the position where the error occurred.
     *
     * @return
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Human readable representation.
     * 
     * @return
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("Syntax error: ");
        str.append(getMessage())
           .append(" at ")
           .append(position)
           .append(" (code: ")
           .append(getCode())
           .append(")!");
        return str.toString();
    }

}
