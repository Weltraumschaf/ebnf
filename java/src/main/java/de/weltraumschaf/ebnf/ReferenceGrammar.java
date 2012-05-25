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

import de.weltraumschaf.ebnf.parser.SyntaxException;
import de.weltraumschaf.ebnf.parser.EbnfScanner;
import de.weltraumschaf.ebnf.parser.EbnfParser;
import de.weltraumschaf.ebnf.ast.nodes.Syntax;
import de.weltraumschaf.ebnf.parser.Factory;
import de.weltraumschaf.ebnf.parser.Parser;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Defines a immutable reference grammar.
 *
 * thus the reference grammar does not change on runtime it is
 * a shared object.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class ReferenceGrammar {

    /**
     * Shared instance.
     */
    private static final ReferenceGrammar INSTANCE = new ReferenceGrammar();
    /**
     * Holds the AST {@ink de.weltraumschaf.ebnf.ast.nodes.Syntax syntax}
     * node for the reference grammar for reuse.
     */
    private static Syntax syntax = null;

    /**
     * Not instantiated from outside.
     */
    private ReferenceGrammar() { }

    /**
     * Returns an immutable reference grammar instance.
     *
     * @return
     */
    public static ReferenceGrammar getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the reference grammar as string.
     *
     * @return
     */
    @Override
    public String toString() {
        return "\"EBNF defined in itself.\" {\n"
             + "    syntax     = [ title ] \"{\" { rule } \"}\" [ comment ] .\n"
             + "    rule       = identifier ( \"=\" | \":\" | \":==\" ) expression ( \".\" | \";\" ) .\n"
             + "    expression = term { \"|\" term } .\n"
             + "    term       = factor { factor } .\n"
             + "    factor     = identifier\n"
             + "               | literal\n"
             + "               | range\n"
             + "               | \"[\" expression \"]\"\n"
             + "               | \"(\" expression \")\"\n"
             + "               | \"{\" expression \"}\" .\n"
             + "    identifier = character { character } .\n"
             + "    range      = character \"..\" character .\n"
             + "    title      = literal .\n"
             + "    comment    = literal .\n"
             + "    literal    = \"\'\" character { character } \"\'\"\n"
             + "               | \'\"\' character { character } \'\"\' .\n"
             + "    character  = \"a\" .. \"z\"\n"
             + "               | \"A\" .. \"Z\"\n"
             + "               | \"0\" .. \"9\" .\n"
             + "}";
    }

    /**
     * Returns the reference syntax as abstract syntax tree.
     *
     * @throws SyntaxException On Syntax errors.
     * @return Returns the grammars {@link Syntax} object.
     */
    public Syntax getSyntax() throws SyntaxException {
        if (null == syntax) {
            final Parser parser = Factory.newParserFromSource(toString());

            try {
                syntax = parser.parse();
            } catch (IOException ex) {
                Logger.getLogger(ReferenceGrammar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return syntax;
    }
}
