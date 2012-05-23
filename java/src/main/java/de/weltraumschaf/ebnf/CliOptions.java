package de.weltraumschaf.ebnf;

import org.apache.commons.cli.*;

/**
 * Available command line arguments.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CliOptions {

    private final class Parser {
        private static final String OPT_SYNTAX    = "s";
        private static final String OPT_OUTPUT    = "o";
        private static final String OPT_FORMAT    = "f";
        private static final String OPT_TEXT_TREE = "t";
        private static final String OPT_DEBUG     = "d";
        private static final String OPT_HELP      = "h";

        public void parse(final String[] args) throws ParseException {
            final CommandLineParser parser = new PosixParser();
            final CommandLine cmd = parser.parse(options, args);

            optSyntax(cmd);
            optFormat(cmd);
            optOutputFile(cmd);
            optTextTree(cmd);
            optDebug(cmd);
            optHelp(cmd);
        }

        private void optSyntax(final CommandLine cmd) {
            if (cmd.hasOption(OPT_SYNTAX)) {
                syntaxFile = cmd.getOptionValue(OPT_SYNTAX);
            }
        }

        private void optFormat(final CommandLine cmd) {
            if (cmd.hasOption(OPT_FORMAT)) {
                final String formatOption = cmd.getOptionValue(OPT_FORMAT);

                if (formatOption.equalsIgnoreCase("xml")) {
                    outputFormat = OutputFormat.XML;
                } else if (formatOption.equalsIgnoreCase("jpg")) {
                    outputFormat = OutputFormat.JPG;
                } else if (formatOption.equalsIgnoreCase("gif")) {
                    outputFormat = OutputFormat.GIF;
                }
            }
        }

        private void optOutputFile(final CommandLine cmd) {
            if (cmd.hasOption(OPT_OUTPUT)) {
                outputFile = cmd.getOptionValue(OPT_OUTPUT);
            } else {
                outputFile = syntaxFile.replace(".ebnf", "." + outputFormat.name().toLowerCase());
            }
        }

        private void optTextTree(final CommandLine cmd) {
            if (cmd.hasOption(OPT_TEXT_TREE)) {
                textTree = true;
            }
        }

        private void optDebug(final CommandLine cmd) {
            if (cmd.hasOption(OPT_DEBUG)) {
                debug = true;
            }
        }

        private void optHelp(final CommandLine cmd) {
            if (cmd.hasOption(OPT_HELP)) {
                help = true;
            }
        }
    }

    private final Options options;

    private OutputFormat outputFormat = OutputFormat.JPG;
    private String syntaxFile   = "";
    private String outputFile   = "";
    private boolean textTree = false;
    private boolean debug    = false;
    private boolean help     = false;

    public CliOptions() {
        options = new Options();
        // w/ argument
        options.addOption(Parser.OPT_SYNTAX, true, "EBNF syntax file to parse.");    // required
        options.addOption(Parser.OPT_OUTPUT, true, "Output file name.");
        options.addOption(Parser.OPT_FORMAT, true, "Output format: xml, jpg, gif.");
        // w/o argument
        options.addOption(Parser.OPT_TEXT_TREE,
                          false,
                          "Prints textual representation of the syntax tree to stdout.");
        options.addOption(Parser.OPT_DEBUG,     false, "Enables debug output.");
        options.addOption(Parser.OPT_HELP,      false, "This help.");
    }

    public void parse(final String[] args) throws ParseException {
        final Parser parser = new Parser();
        parser.parse(args);
    }

    public boolean hasFormat() {
        return null != outputFormat;
    }

    public OutputFormat getFormat() {
        return outputFormat;
    }

    public boolean hasOutputFile() {
        return null != outputFile && !outputFile.isEmpty();
    }

    public String getOutputFile() {
        return outputFile;
    }

    public boolean hasSyntaxFile() {
        return null != syntaxFile && !syntaxFile.isEmpty();
    }

    public String getSyntaxFile() {
        return syntaxFile;
    }

    public boolean isDebug() {
        return debug;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isTextTree() {
        return textTree;
    }

    public void format(final HelpFormatter formatter) {
        formatter.printHelp("ebnf", options);
    }
}
