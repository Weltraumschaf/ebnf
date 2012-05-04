package de.weltraumschaf.ebnf;

import de.weltraumschaf.ebnf.App.ExitCode;

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
        this(message, ExitCode.FATAL_ERROR);
    }

    public Error(String message, ExitCode code) {
        this(message, code.getCode());
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

    public Error(String message, ExitCode code, Throwable cause) {
        this(message, code.getCode(), cause);
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
