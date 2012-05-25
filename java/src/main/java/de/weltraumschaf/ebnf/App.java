package de.weltraumschaf.ebnf;

import de.weltraumschaf.ebnf.ast.nodes.Syntax;
import de.weltraumschaf.ebnf.ide.IdeApp;
import de.weltraumschaf.ebnf.parser.Factory;
import de.weltraumschaf.ebnf.parser.Parser;
import de.weltraumschaf.ebnf.parser.SyntaxException;
import de.weltraumschaf.ebnf.visitor.TextSyntaxTree;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

/**
 * Main application class.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class App {

    private final String[] args;

    private App(final String[] args) {
        this.args = args.clone();
    }

    public static void main(final String[] args) {
        final App app = new App(args);
        app.run();
    }

    private static void exit(final ExitCode code) {
        exit(code.getCode());
    }

    private static void exit(final int code) {
        System.exit(code);
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

    private static void println(final String str) {
        // CHECKSTYLE:OFF
        System.out.println(str);
        // CHECKSTYLE:ON
    }

    private void run() {
        boolean debug = false;

        try {
            final CliOptions options = parseOptions();
            debug = options.isDebug();

            if (options.isHelp()) {
                options.format(new HelpFormatter());
                exit(ExitCode.OK);
            }

            if (options.isIde()) {
                runGuiIde();
            } else {
                runCliApp(options);
            }
        } catch (EbnfException ex) {
            println(ex.getMessage());

            if (debug) {
                ex.printStackTrace(System.out);
            }

            exit(ex.getCode());
        } catch (Exception ex) { //NOPMD
            println("Fatal error!");

            if (debug) {
                ex.printStackTrace(System.out);
            }

            exit(ExitCode.FATAL_ERROR);
        }
    }

    private void runCliApp(final CliOptions options) {
        if (!options.hasSyntaxFile()) {
            println("No syntax file given!");
            exit(ExitCode.NO_SYNTAX);
        }

        final String fileName = options.getSyntaxFile();

        try {
            final Parser parser  = Factory.newParserFromSource(new File(fileName), fileName);
            final Syntax ast     = parser.parse();

            if (options.isTextTree()) {
                final TextSyntaxTree visitor = new TextSyntaxTree();
                ast.accept(visitor);
                println(visitor.getText());
            }
        } catch (SyntaxException ex) {
            println("Syntax error: " + ex.getMessage());

            if (options.isDebug()) {
                ex.printStackTrace(System.out);
            }

            exit(ExitCode.SYNTAX_ERROR);
        } catch (FileNotFoundException ex) {
            println(String.format("Can not read syntax file '%s'!", fileName));

            if (options.isDebug()) {
                ex.printStackTrace(System.out);
            }

            exit(ExitCode.READ_ERROR);
        } catch (IOException ex) {
            println(String.format("Can not read syntax file '%s'!", fileName));

            if (options.isDebug()) {
                ex.printStackTrace(System.out);
            }

            exit(ExitCode.READ_ERROR);
        }

        exit(ExitCode.OK);
    }

    private void runGuiIde() {
        IdeApp.main();
    }
}
