package de.weltraumschaf.ebnf.ast.nodes;

import de.weltraumschaf.ebnf.ast.*;
import de.weltraumschaf.ebnf.visitor.Visitor;

/**
 * Comment node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public final class Comment extends AbstractNode {

    /**
     * The comment string value.
     */
    @Attribute public String value;

    /**
     * Initializes object with empty value and parent node.
     *
     * @param parent
     * @param value
     */
    private Comment(final Node parent, final String value) {
        super(parent, NodeType.COMMENT);
        this.value = value;
    }

    /**
     * Creates new comment node with {@link Null} parent node and empty string value.
     *
     * @return New instance.
     */
    public static Comment newInstance() {
        return newInstance(Null.getInstance());
    }

    /**
     * Creates new comment node with {@link Null} parent node
     *
     * @param value The comment string.
     * @return       New instance.
     */
    public static Comment newInstance(final String value) {
        return newInstance(Null.getInstance(), value);
    }

    /**
     * Creates new comment node with empty string value.
     *
     * @param parent The node's parent.
     * @return        New instance.
     */
    public static Comment newInstance(final Node parent) {
        return newInstance(parent, "");
    }

    /**
     * Creates new comment node.
     *
     * @param parent The node's parent.
     * @param value  The comment string.
     * @return        New instance.
     */
    public static Comment newInstance(final Node parent, final String value) {
        return new Comment(parent, value);
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
            final Comment terminal = (Comment) other;

            if (!value.equals(terminal.value)) {
                result.error("Comment value mismatch: '%s' != '%s'!", value, terminal.value);
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
     * This node has no sub nodes, thus always 1 is returned.
     *
     * @return Returns always 1.
     */
    @Override
    public int depth() {
        return 1;
    }

    /**
     * Human readable string representation.
     *
     * @return String representation.
     */
    @Override
    public String toString() {
        return String.format("<%s value=%s>", getNodeName().toUpperCase(), value);
    }

}
