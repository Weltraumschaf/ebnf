package de.weltraumschaf.ebnf.ast.builder;

import de.weltraumschaf.ebnf.ast.Syntax;

/**
 * Entry point to build an AST.
 *
 * Example:
 * <code>
 * import static de.weltraumschaf.ebnf.ast.builder.SyntaxBuilder.syntax;
 *
 * ...
 *
 * Syntax syntax = syntax("EBNF defined in itself.")
 *     .rule("syntax")
 *         .sequence()
 *             .option()
 *                 .identifier("title")
 *             .end()
 *             .terminal("{")
 *             .loop()
 *                 .identifier("rule")
 *             .end()
 *             .terminal("}")
 *             .option()
 *                 .identifier("comment")
 *             .end()
 *         .end()
 *     .end()
 * .build();
 * ...
 * </code>
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class SyntaxBuilder {

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
    public static RuleBuilder syntax(String title) {
        return syntax(title, Syntax.DEFAULT_META);
    }

    /**
     * Creates the syntax with title and meta.
     *
     * @param title Title of the syntax.
     * @param meta  Meta information of the syntax.
     * @return
     */
    public static RuleBuilder syntax(String title, String meta) {
        Syntax syntax = new Syntax();
        syntax.title  = title;
        syntax.meta   = meta;
        return new RuleBuilder(syntax);
    }

}
