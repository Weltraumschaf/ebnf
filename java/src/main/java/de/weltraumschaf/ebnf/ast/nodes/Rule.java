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
     */
    @Attribute public String name;

    private Rule(final Node parent, final String name) {
        super(parent, NodeType.RULE);
        this.name = name;
    }

    /**
     * Creates an new rule node with a {@link Null} parent node and empty name string.
     *
     * @return New instance.
     */
    public static Rule newInstance() {
        return newInstance(Null.getInstance());
    }

    /**
     * Creates an new rule node with a {@link Null} parent node.
     *
     * @param name The rule name.
     * @return      New instance.
     */
    public static Rule newInstance(final String name) {
        return newInstance(Null.getInstance(), name);
    }

    /**
     * Creates an new rule node with an empty name string.
     *
     * @param parent The parent node.
     * @return        New instance.
     */
    public static Rule newInstance(final Node parent) {
        return newInstance(parent, "");
    }

    /**
     * Creates an new rule node.
     *
     * @param parent The parent node.
     * @param name   The rule name.
     * @return        New instance.
     */
    public static Rule newInstance(final Node parent, final String name) {
        return new Rule(parent, name);
    }

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param other  other  Node to compare against.
     * @param result result Object which collects all equivalence violations.
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
        str.append(String.format("<%s name=%s>", getNodeName().toUpperCase(), name));

        if (hasChildren()) {
            for (Node child : getChildren()) {
                str.append('\n').append(child.toString());
            }
        }

        return str.toString();
    }
}
