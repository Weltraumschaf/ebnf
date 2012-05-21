package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.*;

/**
 * Rule node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Rule extends AbstractComposite {

    /**
     * Name literal of rule.
     *
     * @var
     */
    @Attribute public String name;

    private Rule(final Node parent, final String name) {
        super(parent);
        this.name = name;
    }

    public static Rule newInstance() {
        return newInstance(Null.getInstance());
    }

    public static Rule newInstance(final String name) {
        return newInstance(Null.getInstance(), name);
    }

    public static Rule newInstance(final Node parent) {
        return newInstance(parent, "");
    }

    public static Rule newInstance(final Node parent, final String name) {
        return new Rule(parent, name);
    }

    /**
     * Returns the name of a node.
     *
     * @return
     */
    @Override
    public String getNodeName() {
        return NodeType.RULE.toString();
    }

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param Node         other  Node to compare against.
     * @param Notification result Object which collects all equivalence violations.
     *
     * @return
     */
    @Override
    public void probeEquivalence(final Node other, final Notification result) {
        super.probeEquivalence(other, result);

        final Rule rule = (Rule) other;

        if (!name.equals(rule.name)) {
            result.error("Names of rule differs: '%s' != '%s'!", name, rule.name);
        }
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder();
        str.append(String.format("<RULE name=%s>", name));

        if (hasChildren()) {
            for (Node child : getChildren()) {
                str.append('\n').append(child.toString());
            }
        }

        return str.toString();
    }
}
