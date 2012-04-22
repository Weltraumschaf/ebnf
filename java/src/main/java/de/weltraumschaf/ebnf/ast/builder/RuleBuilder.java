package de.weltraumschaf.ebnf.ast.builder;

import de.weltraumschaf.ebnf.ast.Rule;
import de.weltraumschaf.ebnf.ast.Syntax;

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
    RuleBuilder(Syntax syntax) {
        this.syntax = syntax;
    }

    /**
     * Creates a rule with name.
     *
     * @param name The rule name.
     * @return
     */
    public GenericBuilder<RuleBuilder> rule(String name) {
        Rule rule = new Rule(syntax);
        rule.name = name;
        return new GenericBuilder<RuleBuilder>(this, rule);
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
