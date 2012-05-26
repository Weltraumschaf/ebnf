package de.weltraumschaf.ebnf.cli;

import de.weltraumschaf.ebnf.OutputFormat;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Available command line arguments.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class CliOptions {

    protected final Options options;

    private OutputFormat outputFormat = null;
    private String syntaxFile   = "";
    private String outputFile   = "";
    private boolean textTree = false;
    private boolean debug    = false;
    private boolean help     = false;
    private boolean ide      = false;

    public CliOptions() {
        options = new Options();
        // w/ argument
        options.addOption(OptionsParser.OPT_SYNTAX, true, "EBNF syntax file to parse.");    // required
        options.addOption(OptionsParser.OPT_OUTPUT, true, "Output file name.");
        options.addOption(OptionsParser.OPT_FORMAT, true, "Output format: xml, jpg, gif.");
        // w/o argument
        options.addOption(OptionsParser.OPT_TEXT_TREE,
                          false,
                          "Prints textual representation of the syntax tree to stdout.");
        options.addOption(OptionsParser.OPT_DEBUG,     false, "Enables debug output.");
        options.addOption(OptionsParser.OPT_HELP,      false, "This help.");
        options.addOption(OptionsParser.OPT_IDE, OptionsParser.OPT_IDE_LONG, false, "Starts the GUI IDE.");
    }

    public Options getOptions() {
        return options;
    }

    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    public void setHelp(final boolean help) {
        this.help = help;
    }

    public void setIde(final boolean ide) {
        this.ide = ide;
    }

    public void setOutputFile(final String outputFile) {
        this.outputFile = outputFile;
    }

    public void setOutputFormat(final OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void setSyntaxFile(final String syntaxFile) {
        this.syntaxFile = syntaxFile;
    }

    public void setTextTree(final boolean textTree) {
        this.textTree = textTree;
    }

    public void parse(final String[] args) throws ParseException {
        final OptionsParser parser = new OptionsParser(this);
        parser.parse(args);
    }

    public boolean hasOutputFormat() {
        return null != outputFormat;
    }

    public OutputFormat getOutputFormat() {
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

    public boolean isIde() {
        return ide;
    }

    public void format(final HelpFormatter formatter) {
        formatter.printHelp("ebnf", options);
    }
}
