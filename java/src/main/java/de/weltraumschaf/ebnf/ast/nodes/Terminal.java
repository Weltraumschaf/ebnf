package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.*;
import de.weltraumschaf.ebnf.visitor.Visitor;

/**
 * Terminal node.
 *
 * Has no sub nodes.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Terminal extends AbstractNode {

    /**
     * The literal string value.
     */
    @Attribute public String value;

    /**
     * Initializes object with empty value and parent node.
     *
     * @param parent The parent node.
     * @param value  The terminal value.
     */
    private Terminal(final Node parent, final String value) {
        super(parent);
        this.value = value;
    }

    /**
     * Creates an new terminal node with a {@link Null} parent node and empty value string.
     *
     * @return New instance.
     */
    public static Terminal newInstance() {
        return newInstance(Null.getInstance());
    }

    /**
     * Creates an new terminal node with a {@link Null} parent node.
     *
     * @param value The terminal value.
     * @return       New instance.
     */
    public static Terminal newInstance(final String value) {
        return newInstance(Null.getInstance(), value);
    }

    /**
     * Creates an new terminal node with an empty value string.
     *
     * @param parent The parent node.
     * @return        New instance.
     */
    public static Terminal newInstance(final Node parent) {
        return newInstance(parent, "");
    }

    /**
     * Creates an new terminal node.
     *
     * @param parent
     * @param value
     * @return
     */
    public static Terminal newInstance(final Node parent, final String value) {
        return new Terminal(parent, value);
    }

    /**
     * Returns the name of a node.
     *
     * @return
     */
    @Override
    public String getNodeName() {
        return NodeType.TERMINAL.toString();
    }

    /**
     * Defines method to accept {@link Visitor}.
     *
     * Implements <a href="http://en.wikipedia.org/wiki/Visitor_pattern">Visitor Pattern</a>.
     *
     * @param visitor Object which visits the node.
     */
    @Override
    public void accept(final Visitor visitor) {
        visitor.beforeVisit(this);
        visitor.visit(this);
        visitor.afterVisit(this);
    }

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param other  Node to compare against.
     * @param result Object which collects all equivalence violations.
     */
    @Override
    public void probeEquivalence(final Node other, final Notification result) {
        try {
            final Terminal terminal = (Terminal) other;

            if (!value.equals(terminal.value)) {
                result.error("Terminal value mismatch: '%s' != '%s'!", value, terminal.value);
            }
        } catch (ClassCastException ex) {
            result.error(
                "Probed node types mismatch: '%s' != '%s'!",
                getClass(),
                other.getClass()
            );
        }
    }

    /**
     * Always returns 1.
     *
     * @return
     */
    @Override
    public int depth() {
        return 1;
    }

    @Override
    public String toString() {
        return String.format("<TERMINAL value=%s>", value);
    }
}
