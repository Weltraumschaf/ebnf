package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.visitor.Visitor;

/**
 * Comment node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Comment extends AbstractNode {

    /**
     * The comment string value.
     */
    public String value = "";

    /**
     * Initializes object with empty value and parent node.
     *
     * @param parent The parent node.
     */
    public Comment(Node parent) {
        super(parent);
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
     * Implements {@link http://en.wikipedia.org/wiki/Visitor_pattern Visitor Pattern}.
     *
     * @param Visitor Object which visits te node.
     *
     * @return void
     */
    @Override
    public void accept(Visitor visitor) {
        visitor.beforeVisit(this);
        visitor.visit(this);
        visitor.afterVisit(this);
    }

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param Node         Node to compare against.
     * @param Notification Object which collects all equivalence violations.
     *
     * @return void
     */
    @Override
    public void probeEquivalence(Node other, Notification result) {
        try {
            Comment terminal = (Comment) other;

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
        return String.format("<COMMENT value=%s>", value);
    }

}
