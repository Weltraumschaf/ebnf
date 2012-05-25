package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.*;
import de.weltraumschaf.ebnf.visitor.Visitor;

/**
 * Identifier node.
 *
 * Has no sub nodes.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Identifier extends AbstractNode {

    /**
     * The literal string.
     */
    @Attribute public String value;

    /**
     * Initializes object with value and parent node.
     *
     * @param parent
     * @param value
     */
    private Identifier(final Node parent, final String value) {
        super(parent, NodeType.IDENTIFIER);
        this.value = value;
    }

    /**
     * Creates an new identifier node with a {@link Null} parent node and empty value string.
     *
     * @return New instance.
     */
    public static Identifier newInstance() {
        return newInstance(Null.getInstance());
    }

    /**
     * Creates an new identifier node with a {@link Null} parent node.
     *
     * @param value The identifier name.
     * @return       New instance.
     */
    public static Identifier newInstance(final String value) {
        return newInstance(Null.getInstance(), value);
    }

    /**
     * Creates an new identifier node with an empty value string.
     *
     * @param parent The parent node.
     * @return        New instance.
     */
    public static Identifier newInstance(final Node parent) {
        return newInstance(parent, "");
    }

    /**
     * Creates an new identifier node.
     *
     * @param parent The parent node.
     * @param value  The identifier name.
     * @return        New instance.
     */
    public static Identifier newInstance(final Node parent, final String value) {
        return new Identifier(parent, value);
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
            final Identifier ident = (Identifier) other;

            if (!value.equals(ident.value)) {
                result.error("Identifier value mismatch: '%s' != '%s'!", value, ident.value);
            }
        } catch (ClassCastException ex) {
            result.error("Probed node types mismatch: '%s' != '%s'!", getClass(), other.getClass());
        }
    }

    /**
     * Has no sub nodes, thus always returns 1.
     *
     * @return Always returns 1.
     */
    @Override
    public int depth() {
        return 1;
    }

    /**
     * Generates human readable string representation.
     *
     * @return String representation.
     */
    @Override
    public String toString() {
        return String.format("<%s value=%s>", getNodeName().toUpperCase(), value);
    }
}
