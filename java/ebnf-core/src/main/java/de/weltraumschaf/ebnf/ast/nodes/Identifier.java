package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.AbstractNode;
import de.weltraumschaf.ebnf.ast.Node;
import de.weltraumschaf.ebnf.ast.NodeType;
import de.weltraumschaf.ebnf.ast.Notification;

/**
 * Identifier node.
 *
 * Has no sub nodes.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Identifier extends AbstractNode {

    private static final String ATTR_VALUE = "value";

    /**
     * Initializes object with value and parent node.
     *
     * @param parent
     * @param value
     */
    private Identifier(final Node parent, final String value) {
        super(parent, NodeType.IDENTIFIER);
        setAttribute(ATTR_VALUE, value);
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

            if (!getAttribute(ATTR_VALUE).equals(ident.getAttribute(ATTR_VALUE))) {
                result.error("Identifier value mismatch: '%s' != '%s'!", getAttribute(ATTR_VALUE),
                                                                         ident.getAttribute(ATTR_VALUE));
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

}
