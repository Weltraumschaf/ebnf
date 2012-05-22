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
     * Implements <a href="http://en.wikipedia.org/wiki/Visitor_pattern">Visitor Pattern</a>.
     *
     * @param visitor Object which visits the node.
     */
    public void accept(Visitor visitor);

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param other  Node to compare against.
     * @param result Object which collects all equivalence violations.
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
