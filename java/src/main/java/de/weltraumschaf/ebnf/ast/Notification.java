package de.weltraumschaf.ebnf.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to notify collected AST errors.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Notification {

    /**
     * Collected arrays.
     *
     * @var
     */
    private final List<String> errors = new ArrayList<String>();

    /**
     * Collect an line of error.
     *
     * The first parameter is a sprintf style format string.
     *
     * Example:
     * error(format, arg1, arg2 .. argN)
     *
     * @return void
     */
    public void error(final String format, final Object... args) {
        errors.add(String.format(format, args));
    }

    /**
     * Returns true if no error was collected.
     *
     * @return
     */
    public boolean isOk() {
        return errors.isEmpty();
    }

    /**
     * Returns all errors concatenated as string.
     *
     * @return string
     */
    public String report() {
        if (isOk()) {
            return "";
        }

        final StringBuilder report = new StringBuilder();

        for (int i = 0; i < errors.size(); ++i) {
            if (i > 0) {
                report.append('\n');
            }

            report.append(errors.get(i));
        }
        return report.toString();
    }

}
