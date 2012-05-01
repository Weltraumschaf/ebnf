package de.weltraumschaf.ebnf;

/**
 * Represents an  error which provides an error code.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Error extends Exception {
    
    private static final long serialVersionUID = 1L;

    /**
     * The error code.
     *
     * By default this is -1.
     */
    private int code;

    /**
     * Initializes with error message.
     *
     * Initializes code with -1.
     *
     * @param message Error message.
     */
    public Error(String message) {
        this(message, -1);
    }

    /**
     * Initializes with error message and code.
     *
     * @param message Error message.
     * @param code    Error code.
     */
    public Error(String message, int code) {
        this(message, code, null);
    }

    /**
     * Dedicated constructor.
     *
     * @param message Error message.
     * @param code    Error code.
     * @param cause   Previous errors.
     */
    public Error(String message, int code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Returns the error code.
     *
     * @return
     */
    public int getCode() {
        return code;
    }

}
