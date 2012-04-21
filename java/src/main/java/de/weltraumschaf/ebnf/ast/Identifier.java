package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.visitor.Visitor;

/**
 * Identifier node.
 *
 * Has no sub nodes.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Identifier extends AbstractNode {

    /**
     * The literal string.
     *
     * @var
     */
    public String value = "";

    /**
     * Initializes object with empty value and parent node.
     *
     * @param Node The parent node.
     */
    public Identifier(Node parent) {
        super(parent);
    }

    /**
     * Returns the name of a node.
     *
     * @return
     */
    @Override
    public String getNodeName() {
        return NodeType.IDENTIFIER.toString();
    }

    /**
     * Defines method to accept {@link Visitors}.
     *
     * Implements {@link http://en.wikipedia.org/wiki/Visitor_pattern Visitor Pattern}.
     *
     * @param Visitor Object which visits te node.
     *
     * @return
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
     * @return
     */
    @Override
    public void probeEquivalence(Node other, Notification result) {
        try {
            Identifier ident = (Identifier) other;

            if (!value.equals(ident.value)) {
                result.error("Identifier value mismatch: '%s' != '%s'!", value, ident.value);
            }
        } catch (ClassCastException ex) {
            result.error("Probed node types mismatch: '%s' != '%s'!", getClass(), other.getClass());
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

}
