package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.AbstractComposite;
import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.NodeType;
import de.weltraumschaf.ebnf.ast.Notification;

/**
 * Rule node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Rule extends AbstractComposite {

    private Rule(final Node parent, final String name) {
        super(parent, NodeType.RULE);
        setAttribute("name", name);
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

        if (!getAttribute("name").equals(rule.getAttribute("name"))) {
            result.error("Names of rule differs: '%s' != '%s'!", getAttribute("name"),
                                                                 rule.getAttribute("name"));
        }
    }

    @Override
    public String toString() {
        final StringBuilder str = new StringBuilder(super.toString());
        
        if (hasChildren()) {
            for (Node child : getChildren()) {
                str.append('\n').append(child.toString());
            }
        }

        return str.toString();
    }
}
