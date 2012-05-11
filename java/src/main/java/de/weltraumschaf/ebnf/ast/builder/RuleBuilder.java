package de.weltraumschaf.ebnf.ast.builder;

import de.weltraumschaf.ebnf.ast.nodes.Comment;
import de.weltraumschaf.ebnf.ast.nodes.Rule;
import de.weltraumschaf.ebnf.ast.nodes.Syntax;

/**
 * Sub builder providing methods to create rules and final build the syntax.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class RuleBuilder {

    /**
     * The created syntax.
     */
    private final Syntax syntax;

    /**
     * Sets the syntax to create the rules on.
     *
     * It is not intended to call this constructor from out side the package.
     *
     * @param syntax
     */
    RuleBuilder(final Syntax syntax) {
        this.syntax = syntax;
    }

    /**
     * Creates a rule with name.
     *
     * @param name The rule name.
     * @return
     */
    public GenericBuilder<RuleBuilder> rule(final String name) {
        final Rule rule = Rule.newInstance(syntax, name);
        syntax.addChild(rule);
        return new GenericBuilder<RuleBuilder>(this, rule);
    }

    public RuleBuilder comment(final String value) {
        final Comment comment = Comment.newInstance(syntax, value);
        syntax.addChild(comment);
        return this;
    }

    /**
     * Returns the created syntax.
     *
     * @return
     */
    public Syntax build() {
        return syntax;
    }
}
