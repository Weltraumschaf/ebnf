package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.visitor.Visitor;

/**
 * Terminal node.
 *
 * Has no sub nodes.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public class Terminal extends AbstractNode {

    /**
     * The literal string value.
     */
    public String value = "";

    /**
     * Initializes object with empty value and parent node.
     *
     * @param Node $parent The parent node.
     */
    public Terminal(Node parent) {
        super(parent);
    }

    /**
     * Returns the name of a node.
     *
     * @return string
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
            Terminal terminal = (Terminal) other;

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

}
