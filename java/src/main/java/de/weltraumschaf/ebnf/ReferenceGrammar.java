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
 * Defines a immutable reference grammar.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class ReferenceGrammar {

    /**
     * Shared instance.
     */
    private static final ReferenceGrammar INSTANCE = new ReferenceGrammar();

    /**
     * Not instantiated from outside.
     */
    private ReferenceGrammar() {}

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
        return "\"EBNF defined in itself.\" {\n" +
                "    syntax     = [ title ] \"{\" { rule } \"}\" [ comment ] .\n" +
                "    rule       = identifier ( \"=\" | \":\" | \":==\" ) expression ( \".\" | \";\" ) .\n" +
                "    expression = term { \"|\" term } .\n" +
                "    term       = factor { factor } .\n" +
                "    factor     = identifier\n" +
                "               | literal\n" +
                "               | range\n" +
                "               | \"[\" expression \"]\"\n" +
                "               | \"(\" expression \")\"\n" +
                "               | \"{\" expression \"}\" .\n" +
                "    identifier = character { character } .\n" +
                "    range      = character \"..\" character .\n" +
                "    title      = literal .\n" +
                "    comment    = literal .\n" +
                "    literal    = \"\'\" character { character } \"\'\"\n" +
                "               | \'\"\' character { character } \'\"\' .\n" +
                "    character  = \"a\" .. \"z\"\n" +
                "               | \"A\" .. \"Z\"\n" +
                "               | \"0\" .. \"9\" .\n" +
                "}";
    }

}
