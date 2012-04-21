package de.weltraumschaf.ebnf.ast;

import de.weltraumschaf.ebnf.visitor.Visitor;

/**
 * Interface of an AST node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Node {

    /**
     * Returns the name of a node.
     *
     * @return
     */
    public String getNodeName();

    /**
     * Defines method to accept {@link Visitor}.
     *
     * Implements {@link http://en.wikipedia.org/wiki/Visitor_pattern Visitor Pattern}.
     *
     * @param Visitor Object which visits te node.
     *
     * @return
     */
    public void accept(Visitor visitor);

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param Node         Node to compare against.
     * @param Notification Object which collects all equivalence violations.
     *
     * @return
     */
    public void probeEquivalence(Node other, Notification result);

    /**
     * Returns the depth of the node.
     *
     * Nodes with no child have a depth of 1. Nodes with children have
     * the max depth of the children plus one. The depth is same as the
     * length of of the longest path in the tree.
     *
     * @return
     */
    public int depth();

}
