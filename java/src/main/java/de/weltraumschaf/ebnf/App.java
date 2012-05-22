package de.weltraumschaf.ebnf;

import de.weltraumschaf.ebnf.ast.nodes.Syntax;
import de.weltraumschaf.ebnf.util.ReaderHelper;
import de.weltraumschaf.ebnf.visitor.TextSyntaxTree;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

/**
 * Main application class.
 */
public class App {

    /**
     * Enumerates the exit codes with an associated error code number.
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

    private final String[] args;

    public App(final String[] args) {
        this.args = args.clone();
    }

    public static void main(final String[] args) {
        try {
            final App app = new App(args);
            exit(app.run());
        } catch (EbnfException e) {
            System.out.println(e.getMessage());
            exit(e.getCode());
        } catch (Exception ex) { //NOPMD
            System.out.println("Fatal error!");
            ex.printStackTrace(System.out);
            exit(ExitCode.FATAL_ERROR);
        }
    }

    private static void exit(final ExitCode code) {
        exit(code.getCode());
    }

    private static void exit(final int code) {
        System.exit(code);
    }

    private ExitCode run()  throws EbnfException {
        final CliOptions options = parseOptions();

        if (options.isHelp()) {
            options.format(new HelpFormatter());
            return ExitCode.OK;
        }

        if (!options.hasSyntaxFile()) {
            System.out.println("No syntax file given!");
            return ExitCode.NO_SYNTAX;
        }

        Scanner scanner;
        Parser parser;
        Syntax ast;

        try {
            scanner = new Scanner(ReaderHelper.createFrom(new File(options.getSyntaxFile())));
            parser  = new Parser(scanner);
            ast     = parser.parse();
        } catch (FileNotFoundException ex) {
            System.out.println(String.format("Can not read syntax file '%s'!", options.getSyntaxFile()));

            if (options.isDebug()) {
                ex.printStackTrace(System.out);
            }

            return ExitCode.READ_ERROR;
        } catch (IOException ex) {
            System.out.println(String.format("Can not read syntax file '%s'!", options.getSyntaxFile()));

            if (options.isDebug()) {
                ex.printStackTrace(System.out);
            }

            return ExitCode.READ_ERROR;
        }

        if (options.isTextTree()) {
            final TextSyntaxTree visitor = new TextSyntaxTree();
            ast.accept(visitor);
            System.out.println(visitor.getText());
        }

        return ExitCode.OK;
    }

    private CliOptions parseOptions() throws EbnfException {
        final CliOptions options = new CliOptions();

        try {
            options.parse(args);
        } catch (ParseException ex) {
            throw new EbnfException(ex.getMessage(), 1, ex);
        }

        return options;
    }
}
