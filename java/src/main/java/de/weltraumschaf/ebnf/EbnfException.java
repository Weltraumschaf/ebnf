package de.weltraumschaf.ebnf;

import de.weltraumschaf.ebnf.App.ExitCode;

/**
 * Represents an  error which provides an error code.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class EbnfException extends Exception {

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
    public EbnfException(final String message) {
        this(message, ExitCode.FATAL_ERROR);
    }

    public EbnfException(final String message, final ExitCode code) {
        this(message, code.getCode());
    }

    /**
     * Initializes with error message and code.
     *
     * @param message Error message.
     * @param code    Error code.
     */
    public EbnfException(final String message, final int code) {
        this(message, code, null);
    }

    public EbnfException(final String message, final ExitCode code, final Throwable cause) {
        this(message, code.getCode(), cause);
    }

    /**
     * Dedicated constructor.
     *
     * @param message Error message.
     * @param code    Error code.
     * @param cause   Previous errors.
     */
    public EbnfException(final String message, final int code, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * Returns the error code.
     *
     * @return The error code.
     */
    public int getCode() {
        return code;
    }

}
