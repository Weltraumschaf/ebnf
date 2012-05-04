package de.weltraumschaf.ebnf;

import org.apache.commons.cli.*;

/**
 * Available command line arguments.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CliOptions {

    private final String OPT_SYNTAX    = "s";
    private final String OPT_OUTPUT    = "o";
    private final String OPT_FORMAT    = "f";
    private final String OPT_TEXT_TREE = "t";
    private final String OPT_DEBUG     = "d";
    private final String OPT_HELP      = "h";

    private final Options options;

    private OutputFormat format = OutputFormat.JPG;
    private String syntaxFile   = "";
    private String outputFile   = "";
    private boolean textTree = false;
    private boolean debug    = false;
    private boolean help     = false;

    public CliOptions() {
        options = new Options();
        // w/ argument
        options.addOption(OPT_SYNTAX, true, "EBNF syntax file to parse.");    // required
        options.addOption(OPT_OUTPUT, true, "Output file name.");             // optional
        options.addOption(OPT_FORMAT, true, "Output format: xml, jpg, gif."); // optional
        // w/o argument
        options.addOption(OPT_TEXT_TREE, false, "Prints textual representation of the syntax tree to stdout."); // optional
        options.addOption(OPT_DEBUG,     false, "Enables debug output."); // optional
        options.addOption(OPT_HELP,      false, "This help.");            // optional
    }

    public void parse(String[] args) throws ParseException {
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption(OPT_SYNTAX)) {
            syntaxFile = cmd.getOptionValue(OPT_SYNTAX);
        }

        if (cmd.hasOption(OPT_FORMAT)) {
            String f = cmd.getOptionValue(OPT_FORMAT);

            if (f.equalsIgnoreCase("xml")) {
                format = OutputFormat.XML;
            } else if (f.equalsIgnoreCase("jpg")) {
                format = OutputFormat.JPG;
            } else if (f.equalsIgnoreCase("gif")) {
                format = OutputFormat.GIF;
            }
        }

        if (cmd.hasOption(OPT_OUTPUT)) {
            outputFile = cmd.getOptionValue(OPT_OUTPUT);
        } else {
            outputFile = syntaxFile.replace(".ebnf", "." + format.name().toLowerCase());
        }

        if (cmd.hasOption(OPT_TEXT_TREE)) {
            textTree = true;
        }

        if (cmd.hasOption(OPT_DEBUG)) {
            debug = true;
        }

        if (cmd.hasOption(OPT_HELP)) {
            help = true;
        }
    }

    public boolean hasFormat() {
        return null != format;
    }

    public OutputFormat getFormat() {
        return format;
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

    public void format(HelpFormatter formatter) {
        formatter.printHelp("ebnf", options);
    }
}
