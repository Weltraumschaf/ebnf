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

package de.weltraumschaf.ebnf.cli;

import de.weltraumschaf.ebnf.OutputFormat;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class OptionsParser {

    public static final String OPT_SYNTAX    = "s";
    public static final String OPT_OUTPUT    = "o";
    public static final String OPT_FORMAT    = "f";
    public static final String OPT_TEXT_TREE = "t";
    public static final String OPT_DEBUG     = "d";
    public static final String OPT_HELP      = "h";
    public static final String OPT_IDE       = "i";
    public static final String OPT_IDE_LONG  = "ide";

    private final CliOptions options;

    public OptionsParser(final CliOptions options) {
        this.options = options;
    }

    public void parse(final String[] args) throws ParseException {
        final CommandLineParser parser = new PosixParser();
        final CommandLine cmd = parser.parse(options.getOptions(), args);

        optSyntax(cmd);
        optFormat(cmd);
        optOutputFile(cmd);
        optTextTree(cmd);
        optDebug(cmd);
        optHelp(cmd);
        optIde(cmd);
    }

    private void optSyntax(final CommandLine cmd) {
        if (cmd.hasOption(OPT_SYNTAX)) {
            options.setSyntaxFile(cmd.getOptionValue(OPT_SYNTAX));
        }
    }

    private void optFormat(final CommandLine cmd) {
        if (cmd.hasOption(OPT_FORMAT)) {
            final String formatOption = cmd.getOptionValue(OPT_FORMAT);

            if ("xml".equalsIgnoreCase(formatOption)) {
                options.setOutputFormat(OutputFormat.XML);
            } else if ("jpg".equalsIgnoreCase(formatOption)) {
                options.setOutputFormat(OutputFormat.JPG);
            } else if ("gif".equalsIgnoreCase(formatOption)) {
                options.setOutputFormat(OutputFormat.GIF);
            } else {
                options.setOutputFormat(OutputFormat.JPG);
            }
        }
    }

    private void optOutputFile(final CommandLine cmd) {
        if (cmd.hasOption(OPT_OUTPUT)) {
            options.setOutputFile(cmd.getOptionValue(OPT_OUTPUT));
        } else if (options.hasOutputFormat() && options.hasSyntaxFile()) {
            final String outputFormat = options.getOutputFormat().name().toLowerCase();
            options.setOutputFile(options.getSyntaxFile().replace(".ebnf", "." + outputFormat));
        }
    }

    private void optTextTree(final CommandLine cmd) {
        if (cmd.hasOption(OPT_TEXT_TREE)) {
            options.setTextTree(true);
        }
    }

    private void optDebug(final CommandLine cmd) {
        if (cmd.hasOption(OPT_DEBUG)) {
            options.setDebug(true);
        }
    }

    private void optHelp(final CommandLine cmd) {
        if (cmd.hasOption(OPT_HELP)) {
            options.setHelp(true);
        }
    }

    private void optIde(final CommandLine cmd) {
        if (cmd.hasOption(OPT_IDE) || cmd.hasOption(OPT_IDE_LONG)) {
            options.setIde(true);
        }
    }
}
