package de.weltraumschaf.ebnf.ast.builder;

import de.weltraumschaf.ebnf.ast.nodes.Syntax;

/**
 * Entry point to build an AST.
 *
 * This builder only provides two static methods to start the generation
 * of an syntax AST.
 *
 * You may import the methods statically for convenience:
 * <code>
 * import static de.weltraumschaf.ebnf.ast.builder.SyntaxBuilder.syntax;
 * ...
 * Syntax syntax = syntax("EBNF defined in itself.")
 * ...
 * </code>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class SyntaxBuilder {

    /**
     * It is not intended to create an object from outside.
     *
     * Use the syntax builder method.
     */
    private SyntaxBuilder() {
    }

    /**
     * Creates the syntax with default meta string.
     *
     * @param title Title of the syntax.
     * @return
     */
    public static RuleBuilder syntax(final String title) {
        return syntax(title, Syntax.DEFAULT_META);
    }

    /**
     * Creates the syntax with title and meta.
     *
     * @param title Title of the syntax.
     * @param meta  Meta information of the syntax.
     * @return
     */
    public static RuleBuilder syntax(final String title, final String meta) {
        return new RuleBuilder(Syntax.newInstance(title, meta));
    }

}
