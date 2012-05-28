package de.weltraumschaf.ebnf.ast;

import java.util.Map;

/**
 * Interface of an AST node.
 *
 * @author Sven Strittmatter <weltraumschaf@googlemail.com>
 */
public interface Node extends Visitable {

    /**
     * Returns the name of a node.
     *
     * @return
     */
    String getNodeName();

    /**
     * Probes equivalence of itself against an other node and collects all
     * errors in the passed {@link Notification} object.
     *
     * @param other  Node to compare against.
     * @param result Object which collects all equivalence violations.
     */
    void probeEquivalence(Node other, Notification result);

    /**
     * Returns the depth of the node.
     *
     * Nodes with no child have a depth of 1. Nodes with children have
     * the max depth of the children plus one. The depth is same as the
     * length of of the longest path in the tree.
     *
     * @return
     */
    int depth();

    NodeType getType();

    boolean hasParent();
    Node getParent();
    boolean hasAttributes();
    Map<String, String> getAttributes();
    boolean hasAttribute(String name);
    String getAttribute(String name);
    void setAttribute(String name, String value);
    boolean isType(NodeType checked);
}
