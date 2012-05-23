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

package de.weltraumschaf.ebnf;

/**
 * Enumerates the exit codes with an associated error code number.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public enum ExitCode {
    /**
        * Exit code for everything ok.
        */
    OK(0),
    /**
        * Exit code for read errors.
        */
    READ_ERROR(1),
    /**
        * Exit code for missing syntax file.
        */
    NO_SYNTAX(2),
    /**
        * Exit code for syntax errors.
        */
    SYNTAX_ERROR(3),
    /**
        * Exit code for all other fatal errors.
        */
    FATAL_ERROR(-1);

    /**
        * The exit codes error code number.
        */
    private final int code;

    private ExitCode(final int code) {
        this.code    = code;
    }

    /**
        * Returns the associated error code.
        *
        * Will be != 0 on error.
        *
        * @return The error code.
        */
    public int getCode() {
        return code;
    }

}
